package bdavanzadas.lab1.services;

import bdavanzadas.lab1.dtos.TopSpenderDTO;
import bdavanzadas.lab1.entities.ClientEntity;
import bdavanzadas.lab1.entities.DealerEntity;
import bdavanzadas.lab1.entities.ProductEntity;
import bdavanzadas.lab1.repositories.DealerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import bdavanzadas.lab1.entities.OrdersEntity;
import bdavanzadas.lab1.repositories.OrdersRepository;

import bdavanzadas.lab1.services.UserService;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrdersService {
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private DealerService dealerService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserService userService;

    @Transactional(readOnly = true)
    public List<OrdersEntity> getAllOrders() {
        return ordersRepository.findAll();
    }

    @Transactional
    public void addOrder(OrdersEntity order) {
        ordersRepository.save(order);
    }

    @Transactional
    public void updateOrder(OrdersEntity order) {
        ordersRepository.update(order);
    }

    @Transactional
    public void deleteOrder(int id) {
        ordersRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public OrdersEntity getOrderById(int id) {
        return ordersRepository.findById(id);
    }

    // en el repository se hace un query para obtener todos los pedidos de un cliente
    @Transactional(readOnly = true)
    public List<OrdersEntity> getOrdersByClientId() {
        // Obtener el userId del usuario autenticado
        Long userId = userService.getAuthenticatedUserId();

        // Obtener el clientId asociado al userId
        String sql = "SELECT id FROM clients WHERE user_id = ?";
        Integer clientId = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        if (clientId == null) {
            throw new IllegalArgumentException("No se encontró un cliente asociado al usuario con ID " + userId);
        }

        // Obtener los pedidos del cliente
        return ordersRepository.findByClientId(clientId);
    }

    // obtener los pedidos por ID de cliente
    @Transactional(readOnly = true)
    public List<OrdersEntity> getOrdersByClientId(int clientId) {
        return ordersRepository.findByClientId(clientId);
    }

    // en el repository se hace un query para obtener todos los pedidos de un dealer
    @Transactional(readOnly = true)
    public List<OrdersEntity> getOrdersByDealerId(int dealerId) {
        return ordersRepository.findByDealerId(dealerId);
    }

    // en el repository se hace un query para obtener el cliente que mas gasto
    @Transactional(readOnly = true)
    public TopSpenderDTO getTopSpender() {
        return ordersRepository.getTopSpender();
    }

    /*
    // Método para crear una orden y asociar productos
    @Transactional
    public void createOrderWithProducts(OrdersEntity order, List<Integer> productIds) {
        // Guardar la orden (sin obtener el ID aquí, ya que no lo necesitas en este método)
        ordersRepository.save(order);

        // Recuperar el ID de la orden recién insertada
        int orderId = ordersRepository.getLastInsertedOrderId();

        // Guardar la relación entre la orden y los productos
        ordersRepository.saveOrderProducts(orderId, productIds);
    }*/

    // Método para crear una orden y asociar productos mediante procedimiento almacenado
    // * USAR ESTA FUNCION EN VEZ DE createOrderWithProducts?
    public void createOrderWithProducts(OrdersEntity order, List<Integer> productIds) {
        // Obtener el userId del usuario autenticado
        Long userId = userService.getAuthenticatedUserId();

        // Obtener el clientId asociado al userId
        String sql = "SELECT id FROM clients WHERE user_id = ?";
        Integer clientId = jdbcTemplate.queryForObject(sql, Integer.class, userId);

        if (clientId == null) {
            throw new IllegalArgumentException("No se encontró un cliente asociado al usuario con ID " + userId);
        }

        // Asignar el clientId a la orden
        order.setClientId(clientId);

        // Llamar al procedimiento almacenado
        sql = "CALL register_order_with_products(?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                order.getOrderDate(),
                order.getStatus(),
                order.getClientId(),
                productIds.toArray(new Integer[0]),
                order.getDealerId() != null ? order.getDealerId() : null // Pasar null si dealerId es null
        );
    }

    //RF 04: tiempo promedio entre entrega y pedido por repartidor
    @Transactional
    public List<List<Object>> obtenerPromedioTiempoEntregaPorRepartidor() {
        // Lista donde se almacenarán los resultados
        List<List<Object>> result = new ArrayList<>();

        // Obtener todos los repartidores
        List<DealerEntity> dealers = dealerService.getAllDealers();  // Asegúrate de tener este método en DealerService

        // Recorrer cada repartidor
        for (DealerEntity dealer : dealers) {
            // Obtener el tiempo promedio de entrega para cada repartidor
            float tiempoPromedio = ordersRepository.obtenerTiempoPromedioHoras(dealer.getId());

            // Crear una sublista con el ID del repartidor y su tiempo promedio de entrega
            List<Object> dealerResult = new ArrayList<>();
            dealerResult.add(dealer.getId());  // ID del repartidor
            dealerResult.add(tiempoPromedio);  // Tiempo promedio en horas

            // Añadir la sublista a la lista de resultados
            result.add(dealerResult);
        }

        return result;
    }

    public void markAsDelivered(int orderId) {
        String sql = "UPDATE orders SET status = 'ENTREGADO' WHERE id = ?";
        jdbcTemplate.update(sql, orderId);
    }

    public void markAsFailed(int orderId) {
        String sql = "UPDATE orders SET status = 'FALLIDO' WHERE id = ?";
        jdbcTemplate.update(sql, orderId);
    }

    // Pedidos fallidos por ID de la empresa
    @Transactional(readOnly = true)
    public List<OrdersEntity> findFailedOrdersByCompanyId(int companyId) {
        return ordersRepository.findFailedOrdersByCompanyId(companyId);
    }

    // Pedidos entregados por ID de la empresa
    @Transactional(readOnly = true)
    public List<OrdersEntity> findDeliveredOrdersByCompanyId(int companyId) {
        return ordersRepository.findDeliveredOrdersByCompanyId(companyId);
    }

    // Pedidos por ID de la empresa hola
    @Transactional(readOnly = true)
    public List<OrdersEntity> findOrdersByCompanyId(int companyId) {
        return ordersRepository.findOrdersByCompanyId(companyId);
    }

    @Transactional
    public void updateOrderStatusByDealerId(int orderId, int dealerId, String newStatus) {
        ordersRepository.updateOrderStatusByDealerId(orderId, dealerId, newStatus);
    }

    @Transactional
    public void updateOrderStatus(int orderId, String newStatus) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        jdbcTemplate.update(sql, newStatus, orderId);
    }

    //findProductsByOrderId que está en el repository
    @Transactional(readOnly = true)
    public List<ProductEntity> findProductsByOrderId(int orderId) {
        return ordersRepository.findProductsByOrderId(orderId);
    }
}
