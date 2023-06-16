package com.javalab.product.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_order_item")
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 10)
    private Integer orderItemId;

    @ManyToOne
    @JoinColumn(name = "orderId")
    private OrderMaster orderMaster;

    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;

    @NotNull
    @Min(value = 1, message = "주문 수량은 1 이상이어야 합니다")
    private Integer quantity;

    @NotNull
    @Min(value = 0, message = "상품 가격은 0 이상이어야 합니다")
    private Integer price;
}
