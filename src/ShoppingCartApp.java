import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Product {
    private String name;
    private double price;
    private String description;
    private String category;
    private String imagePath;

    public Product(String name, double price, String description, String category, String imagePath) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.category = category;
        this.imagePath = imagePath;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getImagePath() { return imagePath; }

    @Override
    public String toString() {
        return name + " - ₹" + price;
    }
}

class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getTotalPrice() { return product.getPrice() * quantity; }
}

class User {
    private String username;
    private String password;
    private String email;
    private String address;

    public User(String username, String password, String email, String address) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}

public class ShoppingCartApp extends JFrame {
    private List<Product> products;
    private List<CartItem> cart;
    private Map<String, User> users;
    private String currentUser;
    private JList<String> productList;
    private DefaultListModel<String> productListModel;
    private JList<String> cartList;
    private DefaultListModel<String> cartListModel;
    private JLabel totalLabel;
    private JTextField searchField;
    private JTabbedPane categoryTabs;
    private JLabel productImageLabel;
    private JLabel productDetailsLabel;
    private JSpinner quantitySpinner;
    private JLabel cartItemCountLabel;

    public ShoppingCartApp() {
        products = new ArrayList<>();
        cart = new ArrayList<>();
        users = new HashMap<>();
        initializeProducts();
        loadUsers();
        showLoginPanel();
    }

    private void initializeProducts() {
        products.add(new Product("Laptop", 75000, "High-performance laptop", "Electronics", "resources/images/laptop.jpg"));
        products.add(new Product("Smartphone", 25000, "Latest 5G model", "Electronics", "resources/images/smartphone.jpg"));
        products.add(new Product("Headphones", 5000, "Noise-cancelling headphones", "Electronics", "resources/images/headphones.jpg"));
        products.add(new Product("Smartwatch", 15000, "Fitness tracking smartwatch", "Electronics", "resources/images/smartwatch.jpg"));
        products.add(new Product("Tablet", 30000, "10-inch display tablet", "Electronics", "resources/images/tablet.jpg"));
        products.add(new Product("Java Programming", 800, "Comprehensive Java guide", "Books", "resources/images/java_book.jpg"));
        products.add(new Product("Data Structures", 600, "Learn algorithms", "Books", "resources/images/data_structures.jpg"));
        products.add(new Product("Fiction Novel", 450, "Bestseller novel", "Books", "resources/images/fiction_novel.jpg"));
        products.add(new Product("Science Textbook", 1200, "Physics for college", "Books", "resources/images/science_textbook.jpg"));
        products.add(new Product("History Book", 700, "World history", "Books", "resources/images/history_book.jpg"));
        products.add(new Product("T-Shirt", 500, "Cotton casual t-shirt", "Clothing", "resources/images/tshirt.jpg"));
        products.add(new Product("Jeans", 1500, "Slim fit jeans", "Clothing", "resources/images/jeans.jpg"));
        products.add(new Product("Jacket", 2500, "Winter jacket", "Clothing", "resources/images/jacket.jpg"));
        products.add(new Product("Sneakers", 3000, "Sports sneakers", "Clothing", "resources/images/sneakers.jpg"));
        products.add(new Product("Shirt", 1000, "Formal shirt", "Clothing", "resources/images/shirt.jpg"));
        products.add(new Product("Ocean Breeze", 3500, "Fresh aquatic fragrance", "Perfumes", "resources/images/ocean_breeze.jpg"));
        products.add(new Product("Rose Elegance", 4000, "Floral rose perfume", "Perfumes", "resources/images/rose_elegance.jpg"));
        products.add(new Product("Sandalwood Bliss", 3000, "Woody warm scent", "Perfumes", "resources/images/sandalwood_bliss.jpg"));
        products.add(new Product("Citrus Spark", 2800, "Zesty citrus fragrance", "Perfumes", "resources/images/citrus_spark.jpg"));
        products.add(new Product("Midnight Oud", 4500, "Rich oud perfume", "Perfumes", "resources/images/midnight_oud.jpg"));
    }

    private void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length >= 3) {
                    String address = parts.length > 3 ? parts[3] : "";
                    users.put(parts[0], new User(parts[0], parts[1], parts[2], address));
                }
            }
        } catch (IOException e) {
        }
    }

    private void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt"))) {
            for (User user : users.values()) {
                writer.write(user.getUsername() + ":" + user.getPassword() + ":" + user.getEmail() + ":" + (user.getAddress() != null ? user.getAddress() : ""));
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving user data!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showLoginPanel() {
        setTitle("Login - Shopping Cart");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().removeAll();
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        loginPanel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JTextField usernameField = new JTextField(15);
        usernameField.setToolTipText("Enter your username");
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setToolTipText("Enter your password");
        JButton loginButton = new JButton("Login");
        JButton signupButton = new JButton("Sign Up");
        JButton resetButton = new JButton("Reset Password");
        styleButton(loginButton);
        styleButton(signupButton);
        styleButton(resetButton);
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(usernameLabel, gbc);
        gbc.gridx = 1;
        loginPanel.add(usernameField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        loginPanel.add(passwordField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        loginPanel.add(loginButton, gbc);
        gbc.gridy = 3;
        loginPanel.add(signupButton, gbc);
        gbc.gridy = 4;
        loginPanel.add(resetButton, gbc);
        add(loginPanel, BorderLayout.CENTER);
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (authenticate(username, password)) {
                currentUser = username;
                if (users.get(currentUser).getAddress() == null || users.get(currentUser).getAddress().isEmpty()) {
                    showAddressPanel();
                } else {
                    showMainPanel();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        signupButton.addActionListener(e -> showSignupPanel());
        resetButton.addActionListener(e -> showResetPasswordPanel());
        setLocationRelativeTo(null);
        revalidate();
        repaint();
    }

    private void showSignupPanel() {
        getContentPane().removeAll();
        setTitle("Sign Up - Shopping Cart");
        JPanel signupPanel = new JPanel(new GridBagLayout());
        signupPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        signupPanel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel usernameLabel = new JLabel("Choose a Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JTextField usernameField = new JTextField(15);
        usernameField.setToolTipText("Enter a unique username");
        JLabel passwordLabel = new JLabel("Create a Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setToolTipText("Enter a secure password");
        JLabel emailLabel = new JLabel("Email Address:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JTextField emailField = new JTextField(15);
        emailField.setToolTipText("Enter your email address");
        JButton signupButton = new JButton("Sign Up");
        JButton backButton = new JButton("Back to Login");
        styleButton(signupButton);
        styleButton(backButton);
        gbc.gridx = 0;
        gbc.gridy = 0;
        signupPanel.add(usernameLabel, gbc);
        gbc.gridx = 1;
        signupPanel.add(usernameField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        signupPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        signupPanel.add(passwordField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        signupPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        signupPanel.add(emailField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        signupPanel.add(signupButton, gbc);
        gbc.gridy = 4;
        signupPanel.add(backButton, gbc);
        add(signupPanel, BorderLayout.CENTER);
        signupButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String email = emailField.getText();
            if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (users.containsKey(username)) {
                JOptionPane.showMessageDialog(this, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                users.put(username, new User(username, password, email, ""));
                saveUsers();
                JOptionPane.showMessageDialog(this, "Signup successful! Please login.", "Success", JOptionPane.INFORMATION_MESSAGE);
                showLoginPanel();
            }
        });
        backButton.addActionListener(e -> showLoginPanel());
        revalidate();
        repaint();
    }

    private void showAddressPanel() {
        getContentPane().removeAll();
        setTitle("Enter Delivery Address");
        JPanel addressPanel = new JPanel(new GridBagLayout());
        addressPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        addressPanel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel promptLabel = new JLabel("Please enter your delivery address:");
        promptLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JTextArea addressField = new JTextArea(3, 20);
        addressField.setLineWrap(true);
        addressField.setWrapStyleWord(true);
        addressField.setToolTipText("Enter your full address for delivery");
        JScrollPane addressScroll = new JScrollPane(addressField);
        JButton saveButton = new JButton("Save Address");
        styleButton(saveButton);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        addressPanel.add(promptLabel, gbc);
        gbc.gridy = 1;
        addressPanel.add(addressScroll, gbc);
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        addressPanel.add(saveButton, gbc);
        add(addressPanel, BorderLayout.CENTER);
        saveButton.addActionListener(e -> {
            String address = addressField.getText().trim();
            if (address.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Address cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                users.get(currentUser).setAddress(address);
                saveUsers();
                JOptionPane.showMessageDialog(this, "Address saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                showMainPanel();
            }
        });
        setLocationRelativeTo(null);
        revalidate();
        repaint();
    }

    private void showResetPasswordPanel() {
        getContentPane().removeAll();
        setTitle("Reset Password - Shopping Cart");
        JPanel resetPanel = new JPanel(new GridBagLayout());
        resetPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        resetPanel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JTextField emailField = new JTextField(15);
        emailField.setToolTipText("Enter your email address");
        JButton resetButton = new JButton("Reset Password");
        JButton backButton = new JButton("Back to Login");
        styleButton(resetButton);
        styleButton(backButton);
        gbc.gridx = 0;
        gbc.gridy = 0;
        resetPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        resetPanel.add(emailField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        resetPanel.add(resetButton, gbc);
        gbc.gridy = 2;
        resetPanel.add(backButton, gbc);
        add(resetPanel, BorderLayout.CENTER);
        resetButton.addActionListener(e -> {
            String email = emailField.getText();
            boolean found = false;
            for (User user : users.values()) {
                if (user.getEmail().equals(email)) {
                    found = true;
                    JOptionPane.showMessageDialog(this, "Password reset link sent to " + email + " (simulated).", "Success", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(this, "Email not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            showLoginPanel();
        });
        backButton.addActionListener(e -> showLoginPanel());
        revalidate();
        repaint();
    }

    private boolean authenticate(String username, String password) {
        User user = users.get(username);
        return user != null && user.getPassword().equals(password);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(255, 204, 0));
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(255, 153, 0));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(255, 204, 0));
            }
        });
    }

    private void showMainPanel() {
        getContentPane().removeAll();
        setTitle("Shopping Cart - Welcome, " + currentUser);
        setSize(1000, 700);
        setLayout(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchPanel.setBackground(new Color(240, 240, 240));
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        styleButton(searchButton);
        searchPanel.add(new JLabel("Search Products:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        cartItemCountLabel = new JLabel("Cart Items: 0");
        cartItemCountLabel.setFont(new Font("Arial", Font.BOLD, 12));
        searchPanel.add(cartItemCountLabel);
        JPanel productPanel = new JPanel(new BorderLayout());
        productPanel.setBorder(BorderFactory.createTitledBorder("Products"));
        productPanel.setBackground(new Color(255, 255, 255));
        categoryTabs = new JTabbedPane();
        String[] categories = {"Electronics", "Books", "Clothing", "Perfumes"};
        for (String category : categories) {
            DefaultListModel<String> categoryListModel = new DefaultListModel<>();
            for (Product product : products) {
                if (product.getCategory().equals(category)) {
                    categoryListModel.addElement(product.toString());
                }
            }
            JList<String> categoryList = new JList<>(categoryListModel);
            categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane scrollPane = new JScrollPane(categoryList);
            categoryTabs.addTab(category, scrollPane);
        }
        productList = new JList<>();
        productPanel.add(categoryTabs, BorderLayout.CENTER);
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Product Details"));
        productImageLabel = new JLabel();
        productImageLabel.setHorizontalAlignment(JLabel.CENTER);
        productDetailsLabel = new JLabel("Select a product to view details.");
        productDetailsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        detailsPanel.add(productImageLabel, BorderLayout.CENTER);
        detailsPanel.add(productDetailsLabel, BorderLayout.NORTH);
        JPanel addPanel = new JPanel(new FlowLayout());
        addPanel.setBackground(new Color(255, 255, 255));
        JLabel quantityLabel = new JLabel("Quantity:");
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        JButton addToCartButton = new JButton("Add to Cart");
        styleButton(addToCartButton);
        addPanel.add(quantityLabel);
        addPanel.add(quantitySpinner);
        addPanel.add(addToCartButton);
        productPanel.add(addPanel, BorderLayout.SOUTH);
        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.setBorder(BorderFactory.createTitledBorder("Shopping Cart"));
        cartPanel.setBackground(new Color(255, 255, 255));
        cartListModel = new DefaultListModel<>();
        cartList = new JList<>(cartListModel);
        cartList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane cartScrollPane = new JScrollPane(cartList);
        cartPanel.add(cartScrollPane, BorderLayout.CENTER);
        JPanel cartButtonPanel = new JPanel();
        cartButtonPanel.setBackground(new Color(255, 255, 255));
        JButton removeFromCartButton = new JButton("Remove from Cart");
        JButton checkoutButton = new JButton("Checkout");
        JButton logoutButton = new JButton("Logout");
        styleButton(removeFromCartButton);
        styleButton(checkoutButton);
        styleButton(logoutButton);
        cartButtonPanel.add(removeFromCartButton);
        cartButtonPanel.add(checkoutButton);
        cartButtonPanel.add(logoutButton);
        cartPanel.add(cartButtonPanel, BorderLayout.SOUTH);
        totalLabel = new JLabel("Total: ₹0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        cartPanel.add(totalLabel, BorderLayout.NORTH);
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, productPanel, detailsPanel);
        splitPane.setDividerLocation(400);
        add(searchPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.WEST);
        add(cartPanel, BorderLayout.EAST);
        categoryTabs.addChangeListener(e -> updateProductList());
        searchButton.addActionListener(e -> searchProducts());
        addToCartButton.addActionListener(e -> addToCart());
        removeFromCartButton.addActionListener(e -> removeFromCart());
        checkoutButton.addActionListener(e -> checkout());
        logoutButton.addActionListener(e -> {
            cart.clear();
            showLoginPanel();
        });
        productList.addListSelectionListener(e -> showProductDetails());
        updateProductList();
        updateCartDisplay();
        setLocationRelativeTo(null);
        revalidate();
        repaint();
    }

    private void updateProductList() {
        int selectedTab = categoryTabs.getSelectedIndex();
        if (selectedTab >= 0) {
            String category = categoryTabs.getTitleAt(selectedTab);
            productListModel = new DefaultListModel<>();
            for (Product product : products) {
                if (product.getCategory().equals(category)) {
                    productListModel.addElement(product.toString());
                }
            }
            JList<String> newList = new JList<>(productListModel);
            newList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            productList = newList;
            JScrollPane scrollPane = new JScrollPane(productList);
            categoryTabs.setComponentAt(selectedTab, scrollPane);
        }
    }

    private void showProductDetails() {
        int selectedIndex = productList.getSelectedIndex();
        if (selectedIndex >= 0) {
            String category = categoryTabs.getTitleAt(categoryTabs.getSelectedIndex());
            List<Product> categoryProducts = products.stream()
                    .filter(p -> p.getCategory().equals(category))
                    .collect(Collectors.toList());
            Product selectedProduct = categoryProducts.get(selectedIndex);
            productDetailsLabel.setText("<html><b>" + selectedProduct.getName() + "</b><br>Price: ₹" + selectedProduct.getPrice() + "<br>" + selectedProduct.getDescription() + "</html>");
            try {
                ImageIcon icon = new ImageIcon(selectedProduct.getImagePath());
                Image scaledImage = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                productImageLabel.setIcon(new ImageIcon(scaledImage));
            } catch (Exception e) {
                productImageLabel.setText("Image not found: " + selectedProduct.getImagePath());
            }
        }
    }

    private void searchProducts() {
        String query = searchField.getText().toLowerCase();
        productListModel.clear();
        String category = categoryTabs.getTitleAt(categoryTabs.getSelectedIndex());
        List<Product> filteredProducts = products.stream()
                .filter(p -> p.getCategory().equals(category) && p.getName().toLowerCase().contains(query))
                .collect(Collectors.toList());
        for (Product product : filteredProducts) {
            productListModel.addElement(product.toString());
        }
        productList.setModel(productListModel);
    }

    private void addToCart() {
        int selectedIndex = productList.getSelectedIndex();
        if (selectedIndex >= 0) {
            String category = categoryTabs.getTitleAt(categoryTabs.getSelectedIndex());
            List<Product> categoryProducts = products.stream()
                    .filter(p -> p.getCategory().equals(category))
                    .collect(Collectors.toList());
            Product selectedProduct = categoryProducts.get(selectedIndex);
            int quantity = (Integer) quantitySpinner.getValue();
            for (CartItem item : cart) {
                if (item.getProduct().equals(selectedProduct)) {
                    item.setQuantity(item.getQuantity() + quantity);
                    updateCartDisplay();
                    JOptionPane.showMessageDialog(this, quantity + " x " + selectedProduct.getName() + " added to cart!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
            cart.add(new CartItem(selectedProduct, quantity));
            updateCartDisplay();
            JOptionPane.showMessageDialog(this, quantity + " x " + selectedProduct.getName() + " added to cart!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a product to add to the cart.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeFromCart() {
        int selectedIndex = cartList.getSelectedIndex();
        if (selectedIndex >= 0) {
            CartItem removedItem = cart.remove(selectedIndex);
            updateCartDisplay();
            JOptionPane.showMessageDialog(this, removedItem.getProduct().getName() + " removed from cart!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item to remove from the cart.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCartDisplay() {
        cartListModel.clear();
        double total = 0.0;
        int itemCount = 0;
        for (CartItem item : cart) {
            cartListModel.addElement(item.getProduct().toString() + " | Quantity: " + item.getQuantity());
            total += item.getTotalPrice();
            itemCount += item.getQuantity();
        }
        double discount = total > 5000 ? total * 0.10 : 0;
        totalLabel.setText(String.format("Total: ₹%.2f (Discount: ₹%.2f)", total - discount, discount));
        cartItemCountLabel.setText("Cart Items: " + itemCount);
    }

    private String showDeliveryAddressDialog() {
        JPanel dialogPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel promptLabel = new JLabel("Select or enter delivery address:");
        promptLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JRadioButton savedAddressRadio = new JRadioButton("Use saved address: " + (users.get(currentUser).getAddress() != null ? users.get(currentUser).getAddress() : "None"));
        savedAddressRadio.setEnabled(users.get(currentUser).getAddress() != null && !users.get(currentUser).getAddress().isEmpty());
        JRadioButton newAddressRadio = new JRadioButton("Enter new address:");
        ButtonGroup addressGroup = new ButtonGroup();
        addressGroup.add(savedAddressRadio);
        addressGroup.add(newAddressRadio);
        JTextArea newAddressField = new JTextArea(3, 20);
        newAddressField.setLineWrap(true);
        newAddressField.setWrapStyleWord(true);
        newAddressField.setToolTipText("Enter a new delivery address for this order");
        JScrollPane addressScroll = new JScrollPane(newAddressField);
        newAddressField.setEnabled(false);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        dialogPanel.add(promptLabel, gbc);
        gbc.gridy = 1;
        dialogPanel.add(savedAddressRadio, gbc);
        gbc.gridy = 2;
        dialogPanel.add(newAddressRadio, gbc);
        gbc.gridy = 3;
        dialogPanel.add(addressScroll, gbc);

        newAddressRadio.addActionListener(e -> newAddressField.setEnabled(true));
        savedAddressRadio.addActionListener(e -> newAddressField.setEnabled(false));

        if (savedAddressRadio.isEnabled()) {
            savedAddressRadio.setSelected(true);
        } else {
            newAddressRadio.setSelected(true);
            newAddressField.setEnabled(true);
        }

        int result = JOptionPane.showConfirmDialog(this, dialogPanel, "Delivery Address", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            if (savedAddressRadio.isSelected() && savedAddressRadio.isEnabled()) {
                return users.get(currentUser).getAddress();
            } else if (newAddressRadio.isSelected()) {
                String newAddress = newAddressField.getText().trim();
                if (newAddress.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "New address cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
                return newAddress;
            }
        }
        return null;
    }

    private void checkout() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String deliveryAddress = showDeliveryAddressDialog();
        if (deliveryAddress == null) {
            JOptionPane.showMessageDialog(this, "Checkout cancelled!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder orderSummary = new StringBuilder("Order Summary for " + currentUser + ":\n");
        orderSummary.append("Delivery Address: " + deliveryAddress + "\n\n");
        double total = 0.0;
        for (CartItem item : cart) {
            orderSummary.append(item.getProduct().toString())
                    .append(" | Quantity: ")
                    .append(item.getQuantity())
                    .append("\n");
            total += item.getTotalPrice();
        }
        double discount = total > 5000 ? total * 0.10 : 0;
        orderSummary.append(String.format("Subtotal: ₹%.2f\n", total));
        orderSummary.append(String.format("Discount (10%% for orders > ₹5000): ₹%.2f\n", discount));
        orderSummary.append(String.format("Final Total: ₹%.2f", total - discount));
        JOptionPane.showMessageDialog(this, orderSummary.toString(), "Order Confirmed", JOptionPane.INFORMATION_MESSAGE);
        cart.clear();
        updateCartDisplay();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ShoppingCartApp app = new ShoppingCartApp();
            app.setVisible(true);
        });
    }
}