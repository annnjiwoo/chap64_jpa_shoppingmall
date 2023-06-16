package com.javalab.product.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.javalab.product.dto.OrderItemDTO;
import com.javalab.product.dto.OrderMasterDTO;
import com.javalab.product.dto.PageRequestDTO;
import com.javalab.product.dto.PageResultDTO;
import com.javalab.product.entity.Member;
import com.javalab.product.entity.OrderItem;
import com.javalab.product.entity.OrderMaster;
import com.javalab.product.repository.OrderItemRepository;
import com.javalab.product.repository.OrderMasterRepository;

@Service
public class OrderMasterServiceImpl implements OrderMasterService {

    private final OrderMasterRepository orderMasterRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderMasterServiceImpl(OrderMasterRepository orderMasterRepository,
    							OrderItemRepository orderItemRepository) {
        this.orderMasterRepository = orderMasterRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public PageResultDTO<OrderMasterDTO, OrderMaster> getList(PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("orderId").descending());
        Page<OrderMaster> result = orderMasterRepository.findAll(pageable);
        Function<OrderMaster, OrderMasterDTO> fn = this::entityToDto;
        return new PageResultDTO<>(result, fn);
    }

    @Override
    public OrderMasterDTO read(Integer orderId) {
        Optional<OrderMaster> orderMaster = orderMasterRepository.findById(orderId);
        return orderMaster.map(this::entityToDto).orElse(null);
    }

    /*
     * 저장
     */
    @Override
    public OrderMaster register(OrderMasterDTO orderMasterDTO) {
    	
        OrderMaster orderMaster = dtoToEntity(orderMasterDTO); // DTO -> entity

        // OrderMaster 저장
        OrderMaster savedOrderMaster = orderMasterRepository.save(orderMaster);
        
        // Set the orderId for each OrderItem
        List<OrderItemDTO> orderItems = orderMasterDTO.getOrderItems();
        
        // Master의 주문번호를 저장할 Item에 세팅해줌
        for (OrderItemDTO orderItemDTO : orderItems) {
            orderItemDTO.setOrderId(savedOrderMaster.getOrderId()); 
        }
        
        // Save OrderItemDTOs
        List<OrderItem> orderItemEntities = orderItems.stream()
                .map(this::orderItemDtoToEntity)
                .collect(Collectors.toList());
        
        orderItemRepository.saveAll(orderItemEntities);
        
        return savedOrderMaster;
    }

    // Convert OrderItemDTO to OrderItem entity
    private OrderItem orderItemDtoToEntity(OrderItemDTO orderItemDTO) {
        return null;
    }    
    
    
    /* 
     * 주문 마스터 수정
     *  - 
     */
    @Override
    public void modify(OrderMasterDTO orderMasterDTO) {
        Optional<OrderMaster> orderMaster = orderMasterRepository.findById(orderMasterDTO.getOrderId());

        // 화면에서 전달된 이메일(pk)로 회원 정보 테이블에서 회원 조회
        Member member = Member.builder().email(orderMasterDTO.getEmail()).build();

        orderMaster.ifPresent(orderMasterEntity -> {
            OrderMaster updatedOrderMaster = dtoToEntity(orderMasterDTO);
            updatedOrderMaster.setMember(member);
            orderMasterRepository.save(updatedOrderMaster);
        });
    }
    
    
    
    @Override
    public boolean remove(Integer orderId) {
        Optional<OrderMaster> orderMaster = orderMasterRepository.findById(orderId);
        if (orderMaster.isPresent()) {
            orderMasterRepository.deleteById(orderId);
            return true;
        } else {
            return false;
        }
    }

	@Override
	public void saveOrderMaster(@Valid OrderMasterDTO orderMasterDTO) {
		// TODO Auto-generated method stub
		
	}

//    public OrderMasterDTO entityToDto(OrderMaster orderMaster) {
//        return OrderMasterDTO.builder()
//                .orderId(orderMaster.getOrderId())
//                .customerName(orderMaster.getCustomerName())
//                .shippingAddress(orderMaster.getShippingAddress())
//                .orderDate(orderMaster.getOrderDate())
//                .build();
//    }
//
//    public OrderMaster dtoToEntity(OrderMasterDTO orderMasterDTO) {
//        return OrderMaster.builder()
//                .orderId(orderMasterDTO.getOrderId())
//                .customerName(orderMasterDTO.getCustomerName())
//                .shippingAddress(orderMasterDTO.getShippingAddress())
//                .orderDate(orderMasterDTO.getOrderDate())
//                .build();
//    }
}
