package in.ankit.resumebuilderapi.service;

import java.util.List;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import in.ankit.resumebuilderapi.document.Payment;
import in.ankit.resumebuilderapi.document.User;
import in.ankit.resumebuilderapi.dto.AuthResponse;
import in.ankit.resumebuilderapi.repository.PaymentRepository;
import in.ankit.resumebuilderapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;
    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    private final AuthService authService;
    // private final EmailService emailService;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    public Payment createPaymentOrder(Authentication authentication, String planType) throws RazorpayException {

        log.info("Inside PaymentService - createPaymentOrder()");

        AuthResponse authResponse = authService.getProfile(authentication);

        RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

        String currency = "INR";
        Integer amount = 49900; // Amount in paise for ₹499.00
        String receipt = "rcpt_" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8);

        JSONObject options = new JSONObject();
        options.put("amount", amount);
        options.put("currency", currency);
        options.put("receipt", receipt);

        Order razorpayOrder = razorpayClient.orders.create(options);

        Payment newPayment = Payment.builder()
                .userId(authResponse.getId())
                .razorpayOrderId(razorpayOrder.get("id"))
                .amount(amount)
                .planType(planType)
                .status("created")
                .currency(currency)
                .receipt(receipt)
                .build();

        return paymentRepository.save(newPayment);
    }

    public boolean verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature)
            throws RazorpayException {
        log.info("Inside PaymentService - verifyPayment()");
        JSONObject options = new JSONObject();
        options.put("razorpay_order_id", razorpayOrderId);
        options.put("razorpay_payment_id", razorpayPaymentId);
        options.put("razorpay_signature", razorpaySignature);

        boolean isVerified = com.razorpay.Utils.verifyPaymentSignature(options, razorpayKeySecret);

        if (isVerified) {
            Payment payment = paymentRepository.findByRazorpayOrderId(razorpayOrderId)
                    .orElseThrow(() -> new RuntimeException("Payment not found"));
            payment.setRazorpayPaymentId(razorpayPaymentId);
            payment.setStatus("paid");
            paymentRepository.save(payment);

            upgradeUserSubscription(payment.getUserId(), payment.getPlanType());
        }

        return isVerified;
    }

    private void upgradeUserSubscription(String userId, String planType) {
        log.info("Upgrading user {} subscription to plan: {}", userId, planType);
        User existingUser = userRepository.findById(userId).orElseThrow(() -> {
            throw new RuntimeException("User not found");
        });

        existingUser.setSubscriptionPlan(planType);
        userRepository.save(existingUser);
    }

    public List<Payment> getPaymentHistory(Authentication authentication) {
        log.info("Inside PaymentService - getPaymentHistory()");
        AuthResponse authResponse = authService.getProfile(authentication);
        return paymentRepository.findByUserIdOrderByCreatedAtDesc(authResponse.getId());
    }

    public Payment getPaymentDetails(String orderId, Authentication authentication) {
        log.info("Inside PaymentService - getPaymentDetails()");
        AuthResponse authResponse = authService.getProfile(authentication);
        Payment payment = paymentRepository.findByRazorpayOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (!payment.getUserId().equals(authResponse.getId())) {
            throw new RuntimeException("Unauthorized access to payment details");
        }

        return payment;
    }

}
