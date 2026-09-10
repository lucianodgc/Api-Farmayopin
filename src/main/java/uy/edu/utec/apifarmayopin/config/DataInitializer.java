package uy.edu.utec.apifarmayopin.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uy.edu.utec.apifarmayopin.models.Category;
import uy.edu.utec.apifarmayopin.models.Product;
import uy.edu.utec.apifarmayopin.models.Role;
import uy.edu.utec.apifarmayopin.models.User;
import uy.edu.utec.apifarmayopin.repositories.CategoryRepository;
import uy.edu.utec.apifarmayopin.repositories.ProductRepository;
import uy.edu.utec.apifarmayopin.repositories.UserRepository;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {

        // Usuario Admin
        User admin = new User();
        admin.setEmail("admin@farmayopin.com");
        admin.setPassword(passwordEncoder.encode("Admin123!"));
        admin.setName("Administrador General");
        admin.setRole(Role.ROLE_ADMIN);

        userRepository.save(admin);

        // Usuario Cliente
        User client = new User();
        client.setEmail("lucianodg.candido@gmail.com");
        client.setPassword(passwordEncoder.encode("55415692"));
        client.setName("Luciano Di Giovanni");
        client.setPhone("092098912");
        client.setRole(Role.ROLE_CLIENT);

        userRepository.save(client);

        System.out.println(">>> Usuarios creados exitosamente");

        // Categoría 1
        Category catMedicamentos = new Category();
        catMedicamentos.setName("Medicamentos");
        catMedicamentos.setDescription("Fármacos de venta libre y bajo receta");
        catMedicamentos = categoryRepository.save(catMedicamentos);

        // Categoría 2
        Category catCuidadoPersonal = new Category();
        catCuidadoPersonal.setName("Cuidado Personal");
        catCuidadoPersonal.setDescription("Higiene, dermocosmética y cuidado diario");
        catCuidadoPersonal = categoryRepository.save(catCuidadoPersonal);

        // Categoría 3
        Category catBotiquin = new Category();
        catBotiquin.setName("Botiquín");
        catBotiquin.setDescription("Insumos y materiales de primeros auxilios");
        catBotiquin = categoryRepository.save(catBotiquin);

        // Producto 1
        Product p1 = new Product();
        p1.setName("Paracetamol 500mg");
        p1.setDescription("Analgésico y antipirético para alivio de dolores leves a moderados.");
        p1.setPrice(120.0);
        p1.setStock(50);
        p1.setCategory(catMedicamentos);
        productRepository.save(p1);

        // Producto 2
        Product p2 = new Product();
        p2.setName("Ibuprofeno 400mg");
        p2.setDescription("Antiinflamatorio no esteroideo indicado para dolor muscular y fiebre.");
        p2.setPrice(150.0);
        p2.setStock(30);
        p2.setCategory(catMedicamentos);
        productRepository.save(p2);

        // Producto 3
        Product p3 = new Product();
        p3.setName("Alcohol en Gel 250ml");
        p3.setDescription("Higienizante de manos con 70% de alcohol y aloe vera humectante.");
        p3.setPrice(180.0);
        p3.setStock(100);
        p3.setCategory(catCuidadoPersonal);
        productRepository.save(p3);

        // Producto 4
        Product p4 = new Product();
        p4.setName("Gasas Estériles x10");
        p4.setDescription("Sobres de gasa estéril de algodón para curaciones.");
        p4.setPrice(90.0);
        p4.setStock(40);
        p4.setCategory(catBotiquin);
        productRepository.save(p4);

        System.out.println(">>> Categorías y Productos cargados.");
    }
}