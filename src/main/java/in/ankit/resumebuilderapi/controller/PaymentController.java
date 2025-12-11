package in.ankit.resumebuilderapi.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.RazorpayException;

import in.ankit.resumebuilderapi.document.Payment;
import in.ankit.resumebuilderapi.service.PaymentService;
import in.ankit.resumebuilderapi.util.AppConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, String> orderData, Authentication authentication)
            throws RazorpayException {
        log.info("Creating a new payment order");
        String planType = (String) orderData.get("planType");

        if (!AppConstants.PREMIUM.equalsIgnoreCase(planType)) {
            return ResponseEntity.badRequest().body(Map.of("Message", "Invalid plan type"));
        }

        Payment payment = paymentService.createPaymentOrder(authentication, planType);

        Map<String, Object> response = Map.of(
                "orderId", payment.getRazorpayOrderId(),
                "amount", payment.getAmount(),
                "currency", payment.getCurrency(),
                "planType", payment.getReceipt());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-payment")
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String, Object> orderData) throws RazorpayException {
        log.info("Verifying payment");
        String razorpayOrderId = (String) orderData.get("razorpayOrderId");
        String razorpayPaymentId = (String) orderData.get("razorpayPaymentId");
        String razorpaySignature = (String) orderData.get("razorpaySignature");

        if (Objects.isNull(razorpaySignature) || Objects.isNull(razorpayOrderId) || Objects.isNull(razorpayPaymentId)) {
            return ResponseEntity.badRequest().body(Map.of("Message", "Invalid payment details"));
        }

        boolean isValid = paymentService.verifyPayment(razorpayOrderId, razorpayPaymentId, razorpaySignature);

        if (isValid) {
            return ResponseEntity.ok(Map.of("Message", "Payment verified successfully"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("Message", "Payment verification failed"));
        }
    }

    @GetMapping("/history")
    public ResponseEntity<?> getPaymentHistory(Authentication authentication) {
        log.info("Getting payment history");
        List<Payment> payments = paymentService.getPaymentHistory(authentication);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/get-order/{orderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable String orderId, Authentication authentication) {
        log.info("Getting order details for orderId: {}", orderId);
        return ResponseEntity.ok(paymentService.getPaymentDetails(orderId, authentication));
    }
}
