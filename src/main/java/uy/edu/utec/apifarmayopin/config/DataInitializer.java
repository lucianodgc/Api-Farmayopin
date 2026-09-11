package uy.edu.utec.apifarmayopin.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uy.edu.utec.apifarmayopin.models.*;
import uy.edu.utec.apifarmayopin.repositories.*;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final PurchaseRepository purchaseRepository;

    @Override
    public void run(String... args) throws Exception {

        // 1. Usuario Admin
        User admin = new User();
        admin.setEmail("admin@farmayopin.com");
        admin.setPassword(passwordEncoder.encode("Admin123!"));
        admin.setName("Administrador General");
        admin.setRole(Role.ROLE_ADMIN);
        userRepository.save(admin);

        // 2. Usuario Cliente
        User client = new User();
        client.setEmail("lucianodg.candido@gmail.com");
        client.setPassword(passwordEncoder.encode("55415692"));
        client.setName("Luciano Di Giovanni");
        client.setPhone("092098912");
        client.setRole(Role.ROLE_CLIENT);
        client = userRepository.save(client);

        System.out.println(">>> Usuarios creados exitosamente");

        // 3. Categorías
        Category catMedicamentos = new Category();
        catMedicamentos.setName("Medicamentos");
        catMedicamentos.setDescription("Fármacos de venta libre y bajo receta");
        catMedicamentos = categoryRepository.save(catMedicamentos);

        Category catCuidadoPersonal = new Category();
        catCuidadoPersonal.setName("Cuidado Personal");
        catCuidadoPersonal.setDescription("Higiene, dermocosmética y cuidado diario");
        catCuidadoPersonal = categoryRepository.save(catCuidadoPersonal);

        Category catBotiquin = new Category();
        catBotiquin.setName("Botiquín");
        catBotiquin.setDescription("Insumos y materiales de primeros auxilios");
        catBotiquin = categoryRepository.save(catBotiquin);

        // 4. Productos
        Product p1 = new Product();
        p1.setName("Paracetamol 500mg");
        p1.setDescription("Analgésico y antipirético para alivio de dolores leves a moderados.");
        p1.setPrice(120.0);
        p1.setStock(50);
        p1.setCategory(catMedicamentos);
        p1 = productRepository.save(p1);

        Product p2 = new Product();
        p2.setName("Ibuprofeno 400mg");
        p2.setDescription("Antiinflamatorio no esteroideo indicado para dolor muscular y fiebre.");
        p2.setPrice(150.0);
        p2.setStock(30);
        p2.setCategory(catMedicamentos);
        p2 = productRepository.save(p2);

        Product p3 = new Product();
        p3.setName("Alcohol en Gel 250ml");
        p3.setDescription("Higienizante de manos con 70% de alcohol y aloe vera humectante.");
        p3.setPrice(180.0);
        p3.setStock(100);
        p3.setCategory(catCuidadoPersonal);
        p3 = productRepository.save(p3);

        Product p4 = new Product();
        p4.setName("Gasas Estériles x10");
        p4.setDescription("Sobres de gasa estéril de algodón para curaciones.");
        p4.setPrice(90.0);
        p4.setStock(40);
        p4.setCategory(catBotiquin);
        p4 = productRepository.save(p4);

        System.out.println(">>> Categorías y Productos cargados.");

        // 5. Inicializar Carrito del Cliente con un Producto
        Cart cart = new Cart();
        cart.setUser(client);

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(p3);
        cartItem.setQuantity(2);

        cart.getItems().add(cartItem);
        cartRepository.save(cart);

        System.out.println(">>> Carrito inicial cargado.");

        // 6. Inicializar Histórico de Compra del Cliente
        Purchase purchase = new Purchase();
        purchase.setUser(client);
        purchase.setOrderDate(LocalDateTime.now().minusDays(2));
        purchase.setStatus(Status.PENDING);
        purchase.setAddress("Av. Italia 1234, Montevideo");

        PurchaseItem purchaseItem = new PurchaseItem();
        purchaseItem.setPurchase(purchase);
        purchaseItem.setProduct(p1);
        purchaseItem.setQuantity(3);
        purchaseItem.setPrice(p1.getPrice());

        purchase.getItems().add(purchaseItem);
        purchase.setTotal(p1.getPrice() * 3);

        purchaseRepository.save(purchase);

        System.out.println(">>> Histórico de compras inicial cargado.");
    }
}