package com.vietquan.security.service;

        import com.vietquan.security.entity.CartItems;
        import com.vietquan.security.entity.Order;
        import com.vietquan.security.entity.Product;
        import com.vietquan.security.entity.User;
        import com.vietquan.security.enumPackage.OrderStatus;
        import com.vietquan.security.repository.CartItemsRepository;
        import com.vietquan.security.repository.OrderRepository;
        import com.vietquan.security.repository.ProductRepository;
        import com.vietquan.security.repository.UserRepository;
        import com.vietquan.security.request.AddProductToCartRequest;
        import com.vietquan.security.request.CartItemsRequest;
        import com.vietquan.security.request.OrderRequest;
        import lombok.RequiredArgsConstructor;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.ResponseEntity;
        import org.springframework.stereotype.Service;

        import java.util.List;
        import java.util.Optional;
        import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    @Autowired
    private final CartItemsRepository repository;
    @Autowired
    private final OrderRepository orderRepository;

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final ProductRepository productRepository;

    public ResponseEntity<?> addProductToCart( AddProductToCartRequest request) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(request.getUserId(), OrderStatus.PENDING);
        Optional<CartItems> optionalCartItems = repository.findByProductProductIdAndOrderIdAndUserId(request.getProductId(), activeOrder.getId(), request.getUserId());
        if (optionalCartItems.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } else {
            Optional<Product> product = productRepository.findById(request.getProductId());
            Optional<User> user = userRepository.findById(request.getUserId());
            if (product.isPresent() && user.isPresent()) {
                CartItems cartItems = new CartItems();
                cartItems.setProduct(product.get());
                cartItems.setUser(user.get());
                cartItems.setOrder(activeOrder);
                cartItems.setPrice(product.get().getPrice());
                cartItems.setQuantity(1);
                repository.save(cartItems);
                activeOrder.setTotalAmount(activeOrder.getTotalAmount() + cartItems.getPrice());
                activeOrder.setAmount(activeOrder.getAmount()+cartItems.getPrice());
                activeOrder.getCarts().add(cartItems);
                orderRepository.save(activeOrder);
                return ResponseEntity.status(HttpStatus.CREATED).body("add successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or product not found");
            }
        }
    }
    public OrderRequest getCartByUserId(Integer id){
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(id, OrderStatus.PENDING);
        OrderRequest dto=new OrderRequest();
        List<CartItemsRequest>cartItems=activeOrder.getCarts().stream().map(CartItems::getCartDto).collect(Collectors.toList());
        dto.setAmount(activeOrder.getAmount());
        dto.setId(activeOrder.getId());
        dto.setDiscount(activeOrder.getDiscount());
        dto.setTotalAmount(activeOrder.getTotalAmount());
        dto.setCarts(cartItems);
        return dto;


    }
}
