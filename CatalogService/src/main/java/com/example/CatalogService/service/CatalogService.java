package com.example.CatalogService.service;

import blackfriday.protobuf.EdaMessage;
import com.example.CatalogService.casandra.entity.Product;
import com.example.CatalogService.casandra.repo.ProductRepository;
//import com.example.CatalogService.dto.ProductTagDto;
//import com.example.CatalogService.feign.SearchClient;
import com.example.CatalogService.mysql.entity.SellerProduct;
import com.example.CatalogService.mysql.repo.SellerProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CatalogService {

    @Autowired
    SellerProductRepository sellerProductRepository;
    @Autowired
    ProductRepository productRepository;

//    @Autowired
//    SearchClient searchClient;

    @Autowired
    private KafkaTemplate<String, byte[]> kafkaTemplate;

    public Product registerProduct(
            Long sellerId,
            String name,
            String description,
            Long price,
            Long stockCount,
            List<String> tags
    ) {
        SellerProduct sellerProduct = new SellerProduct(sellerId);
        sellerProductRepository.save(sellerProduct);
        Product product = new Product(sellerProduct.id, sellerId, name, description, price, stockCount, tags);

//        ProductTagDto productTagDto = new ProductTagDto();
//        productTagDto.productId = product.id;
//        productTagDto.tags = tags;
//        searchClient.addTagCache(productTagDto);

        var message = EdaMessage.ProductTags.newBuilder()
                .setProductId(product.id)
                .addAllTags(product.tags)
                .build();

        kafkaTemplate.send("product_tags_added", message.toByteArray());

        return productRepository.save(product);
    }


    public void deleteProduct(Long productId) {
        var product = productRepository.findById(productId);
//        ProductTagDto productTagDto = new ProductTagDto();

        if (product.isPresent()) {
//            productTagDto.productId = product.get().id;
//            productTagDto.tags = product.get().tags;
//            searchClient.removeTagCache(productTagDto);

            var message = EdaMessage.ProductTags.newBuilder()
                    .setProductId(product.get().id)
                    .addAllTags(product.get().tags)
                    .build();

            kafkaTemplate.send("product_tags_removed", message.toByteArray());
        }


        productRepository.deleteById(productId);
        sellerProductRepository.deleteById(productId);
    }


    public List<Product> getProductsBySellerId(Long sellerId) {
        List<SellerProduct> sellerProducts = sellerProductRepository.findBySellerId(sellerId);

        ArrayList<Product> products = new ArrayList<>();
        for (SellerProduct sellerProduct : sellerProducts) {
            var product = productRepository.findById(sellerProduct.id);
            product.ifPresent(products::add);
        }
        return products;
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow();
    }


    public Product decreaseStockCount(Long productId, Long decreaseCount) {
        Product product = productRepository.findById(productId).orElseThrow();
        product.stockCount = product.stockCount - decreaseCount;
        return productRepository.save(product);
    }
}
