package com.example.jpastudy.service;


import com.example.jpastudy.dto.pay.KakaoApproveResponse;
import com.example.jpastudy.dto.pay.KakaoReadyResponse;
import com.example.jpastudy.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Transactional
public class KakaoPayService {
    static final String cid="TC0ONETIME";

    @Value("${ADMIN_KEY}")
     String adminKey;

    private KakaoReadyResponse kakaoReadyResponse;



    public KakaoReadyResponse kakaoPayReady(Order order, String email){ // 결제 요청
        System.out.println("카카오페이제발 ~ ");
        MultiValueMap<String, String> parameters=new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("partner_order_id", "가맹점 주문 번호");
        parameters.add("partner_user_id", email);
        parameters.add("item_name", String.valueOf(order.getId()));
        parameters.add("quantity", String.valueOf(order.getOrderItems().size()));
        parameters.add("total_amount", String.valueOf(order.getTotalPrice()));
        parameters.add("vat_amount", "1000");
        parameters.add("tax_free_amount", "200");
        parameters.add("approval_url", "http://localhost:8080/orders");
        parameters.add("cancel_url","http://localhost:8080/orders");
        parameters.add("fail_url","http://localhost:8080/orders");

        HttpEntity<MultiValueMap<String, String>> requestEntity=new HttpEntity<>(parameters, this.getHeaders());

        RestTemplate restTemplate=new RestTemplate();

        kakaoReadyResponse=restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/ready",
                    requestEntity,
                    KakaoReadyResponse.class
        );
        System.out.println("끝 ~ response:"+kakaoReadyResponse);
        return kakaoReadyResponse;
    }

    private HttpHeaders getHeaders(){
        HttpHeaders httpHeaders=new HttpHeaders();
        String auth="KakaoAK "+adminKey;

        httpHeaders.set("Authorization", auth);
        httpHeaders.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        return httpHeaders;
    }

    public KakaoApproveResponse approvePayment(String pgToken){
        MultiValueMap<String, String> parameters=new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("tid", kakaoReadyResponse.getTid());
        parameters.add("partner_order_id", "가맹점 주문 번호");
        parameters.add("partner_user_id", "가맹점 회원 ID");
        parameters.add("pg_token", pgToken);

        RestTemplate restTemplate=new RestTemplate();

        HttpEntity<MultiValueMap<String, String>> requestEntity=new HttpEntity<>(parameters, this.getHeaders());

        KakaoApproveResponse response=restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/approve",
                requestEntity,
                KakaoApproveResponse.class
        );
        return response;
    }
}
