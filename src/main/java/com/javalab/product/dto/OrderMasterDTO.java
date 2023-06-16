package com.javalab.product.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderMasterDTO {
	
	// 주문번호
    private Integer orderId;
    
    // 이메일이 회원ID와 같음
    @NotNull(message = "Member ID 필수 입력")
    private String email;

    // 배송지 주소
    @NotEmpty(message = "배송 주소 필수 입력")
    private String address;
    
    // 주문 상품들
    @NotEmpty(message = "주문 아이템 필수 입력")
    @Valid
    private List<OrderItemDTO> orderItems;

    private LocalDateTime regDate;
    private LocalDateTime modDate;
    
}
