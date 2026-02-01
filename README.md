# The Ultimate Spring Boot Course

This repository contains the starter project for Part 2 of my Spring Boot course:

[https://codewithmosh.com/p/spring-boot-building-apis](https://codewithmosh.com/p/spring-boot-building-apis)

## About this Repository 

This project is based on the final project from Part 1 of the course, but I’ve cleaned it up and removed unnecessary playground code so we can focus on building APIs in Part 2.

You’ll be cloning this repository and coding along with me as we extend the project.

To get started, clone the repository to your local machine:

```sh
git clone https://github.com/mosh-hamedani/spring-api-starter

cd spring-api
```

---

## 🛒 Shopping Cart Implementation Summary

### **What We Built**
We implemented a complete shopping cart system for the Spring Boot e-commerce application with the following components:

### **📁 Files Created/Modified**

**Entities:**
- `Cart.java` - Main cart entity with UUID primary key, creation date, and relationship to cart items
- `CartItem.java` - Individual cart items linking products to carts with quantities

**Controller:**
- `CartController.java` - REST API endpoints for cart operations (GET, POST, PUT, DELETE)

**Data Transfer Objects (DTOs):**
- `CartDto.java` - Cart representation for API responses
- `CartItemDto.java` - Cart item representation for API responses  
- `CartProductDto.java` - Product information within cart context
- `AddItemToCart.java` - DTO for adding items to cart
- `UpdateCartItem.java` - DTO for updating item quantities with validation

**Repository:**
- `CartRepository.java` - JPA repository with custom query for fetching carts with items

**Mapper:**
- `CartMapper.java` - MapStruct mapper for entity-DTO conversions

**Database:**
- `V2__create_cart_tables.sql` - Flyway migration creating `carts` and `cart_items` tables

### **🔧 Key Features Implemented**

**Database Design:**
- UUID-based cart identification for security
- Proper foreign key relationships with cascade delete
- Unique constraint on cart-product combinations
- Automatic UUID generation trigger
- Default quantity of 1 for new items

**Business Logic:**
- Automatic quantity increment when adding existing products
- Total price calculation at cart and item level
- Validation on quantity updates (1-100 items)
- Proper entity lifecycle management with `@PrePersist`

**API Capabilities:**
- Create new carts
- Add products to carts
- Update item quantities
- Remove items from carts
- View cart contents with calculated totals
- Fetch carts with product details included

### **🎯 Technical Highlights**

- **JPA Relationships**: Proper `@OneToMany` and `@ManyToOne` mappings with lazy loading
- **Validation**: Bean validation annotations for data integrity
- **DTO Pattern**: Clean separation between internal entities and API contracts
- **MapStruct Integration**: Efficient mapping between entities and DTOs
- **Database Migration**: Version-controlled schema changes with Flyway
- **RESTful Design**: Standard HTTP methods and status codes

### **📊 What Users Can Do Now**

1. **Create a shopping cart** - Automatically gets a UUID and creation date
2. **Add products** - System handles duplicate products by incrementing quantity
3. **Update quantities** - Validated updates between 1-100 items
4. **View cart** - See all items with individual and total prices
5. **Remove items** - Clean deletion with proper cascade handling

The implementation follows Spring Boot best practices and provides a solid foundation for e-commerce functionality.

### **🚀 API Endpoints**

The shopping cart system provides the following REST endpoints:

- `GET /api/carts/{id}` - Retrieve cart with all items and calculated totals
- `POST /api/carts` - Create a new shopping cart
- `POST /api/carts/{id}/items` - Add product to cart (auto-handles duplicates)
- `PUT /api/carts/{id}/items/{productId}` - Update item quantity (1-100 validation)
- `DELETE /api/carts/{id}/items/{productId}` - Remove item from cart

### **📝 Commit Information**
- **Commit Hash**: `cc83e4b`
- **Date**: February 2, 2026
- **Message**: "feat: implement shopping cart functionality"
