package com.example.imersaospring.services;

import com.example.imersaospring.entities.Sale;
import com.example.imersaospring.repositories.SaleRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    // @Value busca o valor da variável de ambiente especificada no application.properties
    // busca o valor e traz para a variável abaixo
    @Value("${twilio.sid}")
    private String twilioSid;

    @Value("${twilio.key}")
    private String twilioKey;

    @Value("${twilio.phone.from}")
    private String twilioPhoneFrom;

    @Value("${twilio.phone.to}")
    private String twilioPhoneTo;

    // Necessário declarar o SaleRepository para fazer a consulta no banco de dados
    @Autowired
    private SaleRepository saleRepository;

    // Enviar SMS pelo ID. Parâmetro id foi passado no Controller para o método sendSms(id)
    // Abaixo colocamos o argumento id dentro do sendSms() para podermos buscar pelo id
    public void sendSms(Long saleId) {

        Sale sale = saleRepository.findById(saleId).get();

        // Formato date
        String date = sale.getDate().getMonthValue() + "/" + sale.getDate().getYear();

        // Variável para personalizar a mensagem do SMS
        String msg = "O Vendedor: " + sale.getSellerName() + " foi destaque em " + date
                + " com um total de R$ " + String.format("%.2f", sale.getAmount());

        Twilio.init(twilioSid, twilioKey);

        PhoneNumber to = new PhoneNumber(twilioPhoneTo);
        PhoneNumber from = new PhoneNumber(twilioPhoneFrom);

        Message message = Message.creator(to, from, msg).create();

        System.out.println(message.getSid());
    }

}
