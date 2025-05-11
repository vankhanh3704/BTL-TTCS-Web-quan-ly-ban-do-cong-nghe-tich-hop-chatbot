package com.example.e_commerce.technology.service.impl;

import com.example.e_commerce.technology.Entity.CategoryEntity;
import com.example.e_commerce.technology.Entity.ProductEntity;
import com.example.e_commerce.technology.Entity.ProductImageEntity;
import com.example.e_commerce.technology.Enum.ErrorCode;
import com.example.e_commerce.technology.exception.AppException;
import com.example.e_commerce.technology.mapper.ProductMapper;
import com.example.e_commerce.technology.model.dto.ProductQueryParams;
import com.example.e_commerce.technology.model.request.ProductRequest;
import com.example.e_commerce.technology.model.request.ProductSearchRequest;
import com.example.e_commerce.technology.model.response.ProductResponse;
import com.example.e_commerce.technology.repository.CategoryRepository;
import com.example.e_commerce.technology.repository.ProductRepository;
import com.example.e_commerce.technology.repository.ProductSpecifications;
import com.example.e_commerce.technology.service.IProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequiredArgsConstructor
public class ProductService implements IProductService {
    ProductRepository productRepository;
    ProductMapper productMapper;
    CategoryRepository categoryRepository;
    String uploadDir = "./uploads";

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        // Kiểm tra tên sản phẩm trùng lặp
        if (productRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.PRODUCT_EXISTED);
        }

        // Kiểm tra danh mục
        CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        // Ánh xạ sang ProductEntity
        ProductEntity productEntity = productMapper.toProduct(request);
        productEntity.setCategory(category);
        productEntity.setImages(new ArrayList<>()); // Khởi tạo danh sách images

        // Xử lý upload hình ảnh nếu có
        List<String> imageUrls = new ArrayList<>();
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }

            for (MultipartFile file : request.getImages()) {
                try {
                    // Tối ưu hóa ảnh
                    BufferedImage originalImage = ImageIO.read(file.getInputStream());

                    // Resize ảnh chính (800px)
                    BufferedImage mainImage = Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH, 800);
                    String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
                    Path mainPath = Paths.get(uploadDir, fileName);
                    ImageIO.write(mainImage, "jpg", mainPath.toFile());
                    String mainUrl = "/uploads/" + fileName;

                    // Tạo thumbnail (200px)
                    BufferedImage thumbnail = Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH, 200);
                    String thumbnailName = UUID.randomUUID() + "-thumb-" + file.getOriginalFilename();
                    Path thumbPath = Paths.get(uploadDir, thumbnailName);
                    ImageIO.write(thumbnail, "jpg", thumbPath.toFile());

                    // Lưu vào ProductImageEntity
                    ProductImageEntity image = ProductImageEntity.builder()
                            .image_url(mainUrl)
                            .altText(file.getOriginalFilename())
                            .product(productEntity)
                            .build();
                    productEntity.getImages().add(image);
                    imageUrls.add(mainUrl);
                } catch (IOException e) {
                    log.error("Failed to upload image: {}", file.getOriginalFilename(), e);
                    throw new AppException(ErrorCode.IMAGE_UPLOAD_FAILED);
                }
            }
        }

        // Lưu sản phẩm vào cơ sở dữ liệu
        productEntity = productRepository.save(productEntity);
        return productMapper.toProductResponse(productEntity);
    }



    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        // Tìm sản phẩm
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        // Kiểm tra tên sản phẩm trùng (ngoại trừ chính sản phẩm này)
        if (!product.getName().equals(request.getName()) && productRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.PRODUCT_EXISTED);
        }

        // Kiểm tra danh mục
        CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        // Cập nhật các trường
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setStock(request.getStock());
        product.setCategory(category);

        // Xử lý upload hình ảnh mới (nếu có)
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }

            for (MultipartFile file : request.getImages()) {
                try {
                    BufferedImage originalImage = ImageIO.read(file.getInputStream());
                    BufferedImage mainImage = Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH, 800);
                    String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
                    Path mainPath = Paths.get(uploadDir, fileName);
                    ImageIO.write(mainImage, "jpg", mainPath.toFile());
                    String mainUrl = "/uploads/" + fileName;

                    BufferedImage thumbnail = Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH, 200);
                    String thumbnailName = UUID.randomUUID() + "-thumb-" + file.getOriginalFilename();
                    Path thumbPath = Paths.get(uploadDir, thumbnailName);
                    ImageIO.write(thumbnail, "jpg", thumbPath.toFile());

                    ProductImageEntity image = ProductImageEntity.builder()
                            .image_url(mainUrl)
                            .altText(file

                                    .getOriginalFilename())
                            .product(product)
                            .build();
                    product.getImages().add(image);
                } catch (IOException e) {
                    log.error("Failed to upload image: {}", file.getOriginalFilename(), e);
                    throw new AppException(ErrorCode.IMAGE_UPLOAD_FAILED);
                }
            }
        }

        // Lưu sản phẩm
        productRepository.save(product);
        return productMapper.toProductResponse(product);
    }


    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(productMapper::toProductResponse);
    }

    public Page<ProductResponse> searchProducts(ProductSearchRequest request, Pageable pageable) {
        log.info("Searching products with search request: {}", request);
        request.validate();
        Page<ProductEntity> productPage = productRepository.findAll(ProductSpecifications.withSearchRequest(request), pageable);
        log.info("Found {} products", productPage.getTotalElements());
        return productPage.map(productMapper::toProductResponse);
    }

    @Override
    public Page<ProductResponse> searchProducts(ProductQueryParams params) {
        log.info("Searching products with query params: {}", params);

        // Xử lý sort
        String[] sortParts = params.getSort().split(",");
        String sortField = sortParts[0];
        Sort sortObj = Sort.by(sortField);
        if (sortParts.length > 1 && sortParts[1].equalsIgnoreCase("desc")) {
            sortObj = sortObj.descending();
        }

        // Tạo Pageable
        Pageable pageable = PageRequest.of(params.getPage(), params.getSize(), sortObj);

        // Xây dựng ProductSearchRequest
        ProductSearchRequest request = ProductSearchRequest.builder()
                .name(params.getName())
                .categoryId(params.getCategoryId())
                .priceFrom(params.getPriceFrom())
                .priceTo(params.getPriceTo())
                .build();

        // Validate và truy vấn
        request.validate();
        Page<ProductEntity> productPage = productRepository.findAll(ProductSpecifications.withSearchRequest(request), pageable);
        log.info("Found {} products", productPage.getTotalElements());
        return productPage.map(productMapper::toProductResponse);
    }

    public List<String> uploadProductImages(Long productId, List<MultipartFile> files) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        List<String> imageUrls = new ArrayList<>();
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }

        for (MultipartFile file : files) {
            try {
                // Tối ưu hóa ảnh
                BufferedImage originalImage = ImageIO.read(file.getInputStream());

                // Resize ảnh chính (800px)
                BufferedImage mainImage = Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH, 800);
                String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
                Path mainPath = Paths.get(uploadDir, fileName);
                ImageIO.write(mainImage, "jpg", mainPath.toFile());
                String mainUrl = "/uploads/" + fileName;


                // Tạo thumbnail (200px)
                BufferedImage thumbnail = Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH, 200);
                String thumbnailName = UUID.randomUUID() + "-thumb-" + file.getOriginalFilename();
                Path thumbPath = Paths.get(uploadDir, thumbnailName);
                ImageIO.write(thumbnail, "jpg", thumbPath.toFile());
                String thumbUrl = "/uploads/" + thumbnailName;

                // lưu vào database
                ProductImageEntity image = ProductImageEntity.builder()
                        .image_url(mainUrl)
                        .altText(file.getOriginalFilename())
                        .product(product)
                        .build();
                product.getImages().add(image);
                imageUrls.add(mainUrl);
                product.getImages().add(image);
            } catch (IOException e) {
                log.error("Failed to upload image: {}", file.getOriginalFilename(), e);
                throw new AppException(ErrorCode.IMAGE_UPLOAD_FAILED);
            }
        }

        productRepository.save(product);
        return imageUrls;
    }

    @Override
    public void deleteProductImage(Long productId, Long imageId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        ProductImageEntity image = product.getImages().stream()
                .filter(img -> img.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.IMAGE_NOT_FOUND));

        // Xóa file trên server (local)
        try {
            Files.deleteIfExists(Paths.get(image.getImage_url().replace("/uploads/", uploadDir + "/")));
        } catch (IOException e) {
            log.error("Failed to delete image file: {}", image.getImage_url(), e);
        }

        // Xóa ảnh khỏi danh sách
        product.getImages().remove(image);
        productRepository.save(product);
    }
}
