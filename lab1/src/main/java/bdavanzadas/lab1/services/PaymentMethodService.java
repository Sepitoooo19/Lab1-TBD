package bdavanzadas.lab1.services;

import bdavanzadas.lab1.entities.PaymentMethodEntity;
import bdavanzadas.lab1.repositories.PaymentMethodRepostitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PaymentMethodService {
    @Autowired
    private PaymentMethodRepostitory paymentMethodRepository;

    @Transactional(readOnly = true)
    public List<PaymentMethodEntity> getallPaymentMethods() {
        return paymentMethodRepository.findAll();
    }
    @Transactional(readOnly = true)
    public PaymentMethodEntity getPaymentMethodById(int id) {
        return paymentMethodRepository.findById(id);
    }
    @Transactional(readOnly = true)
    public PaymentMethodEntity getPaymentMethodByType(String type) {
        return paymentMethodRepository.findByType(type);
    }
    @Transactional
    public void savePaymentMethod(PaymentMethodEntity paymentMethodEntity) {
        paymentMethodRepository.save(paymentMethodEntity);
    }
    @Transactional
    public void deletePaymentMethod(int id) {
        paymentMethodRepository.deleteById(id);
    }
    @Transactional
    public void updatePaymentMethod(PaymentMethodEntity paymentMethodEntity) {
        paymentMethodRepository.update(paymentMethodEntity);
    }
}
