package co.edu.itp.ciecyt.web.rest;

import co.edu.itp.ciecyt.repository.ElementoProyectoRepository;
import co.edu.itp.ciecyt.service.ElementoProyectoService;
import co.edu.itp.ciecyt.service.dto.ElementoProyectoDTO;
import co.edu.itp.ciecyt.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link co.edu.itp.ciecyt.domain.ElementoProyecto}.
 */
@RestController
@RequestMapping("/api/elemento-proyectos")
public class ElementoProyectoResource {

    private static final Logger log = LoggerFactory.getLogger(ElementoProyectoResource.class);

    private static final String ENTITY_NAME = "elementoProyecto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Value("${file.upload-dir}") // Ruta donde se almacenarán las imágenes
    private String uploadDir;

    private final ElementoProyectoService elementoProyectoService;

    private final ElementoProyectoRepository elementoProyectoRepository;

    public ElementoProyectoResource(
        ElementoProyectoService elementoProyectoService,
        ElementoProyectoRepository elementoProyectoRepository
    ) {
        this.elementoProyectoService = elementoProyectoService;
        this.elementoProyectoRepository = elementoProyectoRepository;
    }

    /**
     * {@code POST  /elemento-proyectos} : Create a new elementoProyecto.
     *
     * @param elementoProyectoDTO the elementoProyectoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new elementoProyectoDTO, or with status {@code 400 (Bad Request)} if the elementoProyecto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ElementoProyectoDTO> createElementoProyecto(@Valid @RequestBody ElementoProyectoDTO elementoProyectoDTO)
        throws URISyntaxException {
        log.debug("REST request to save ElementoProyecto : {}", elementoProyectoDTO);
        if (elementoProyectoDTO.getId() != null) {
            throw new BadRequestAlertException("A new elementoProyecto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        elementoProyectoDTO = elementoProyectoService.save(elementoProyectoDTO);
        return ResponseEntity.created(new URI("/api/elemento-proyectos/" + elementoProyectoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, elementoProyectoDTO.getId().toString()))
            .body(elementoProyectoDTO);
    }

    /**
     * {@code PUT  /elemento-proyectos/:id} : Updates an existing elementoProyecto.
     *
     * @param id the id of the elementoProyectoDTO to save.
     * @param elementoProyectoDTO the elementoProyectoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated elementoProyectoDTO,
     * or with status {@code 400 (Bad Request)} if the elementoProyectoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the elementoProyectoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ElementoProyectoDTO> updateElementoProyecto(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ElementoProyectoDTO elementoProyectoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ElementoProyecto : {}, {}", id, elementoProyectoDTO);
        if (elementoProyectoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, elementoProyectoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!elementoProyectoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        elementoProyectoDTO = elementoProyectoService.update(elementoProyectoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, elementoProyectoDTO.getId().toString()))
            .body(elementoProyectoDTO);
    }

    /**
     * {@code PATCH  /elemento-proyectos/:id} : Partial updates given fields of an existing elementoProyecto, field will ignore if it is null
     *
     * @param id the id of the elementoProyectoDTO to save.
     * @param elementoProyectoDTO the elementoProyectoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated elementoProyectoDTO,
     * or with status {@code 400 (Bad Request)} if the elementoProyectoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the elementoProyectoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the elementoProyectoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ElementoProyectoDTO> partialUpdateElementoProyecto(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ElementoProyectoDTO elementoProyectoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ElementoProyecto partially : {}, {}", id, elementoProyectoDTO);
        if (elementoProyectoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, elementoProyectoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!elementoProyectoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ElementoProyectoDTO> result = elementoProyectoService.partialUpdate(elementoProyectoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, elementoProyectoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /elemento-proyectos} : get all the elementoProyectos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of elementoProyectos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ElementoProyectoDTO>> getAllElementoProyectos(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of ElementoProyectos");
        Page<ElementoProyectoDTO> page = elementoProyectoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /elemento-proyectos/:id} : get the "id" elementoProyecto.
     *
     * @param id the id of the elementoProyectoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the elementoProyectoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ElementoProyectoDTO> getElementoProyecto(@PathVariable("id") Long id) {
        log.debug("REST request to get ElementoProyecto : {}", id);
        Optional<ElementoProyectoDTO> elementoProyectoDTO = elementoProyectoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(elementoProyectoDTO);
    }

    /**
     * {@code DELETE  /elemento-proyectos/:id} : delete the "id" elementoProyecto.
     *
     * @param id the id of the elementoProyectoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteElementoProyecto(@PathVariable("id") Long id) {
        log.debug("REST request to delete ElementoProyecto : {}", id);
        elementoProyectoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code POST  /upload} : Upload an image.
     *
     * Handles the upload of an image file. Validates the file type and stores it in the
     * server's designated upload directory. Returns the publicly accessible URL of the
     * uploaded image.
     *
     * @param file the image file to upload.
     * @return the URL of the uploaded image or an error message.
     */

    /**
     * {@code POST  /upload} : Upload an image.
     *
     * Handles the upload of an image file. Validates the file type and stores it in the
     * server's designated upload directory. Returns the publicly accessible URL of the
     * uploaded image.
     *
     * @param file the image file to upload.
     * @return the URL of the uploaded image or an error message.
     */
    public class UploadedImage {

        private String src;
        private String filename;
        private int width;
        private int height;

        public UploadedImage(String src, String filename, int width, int height) {
            this.src = src;
            this.filename = filename;
            this.width = width;
            this.height = height;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }

    @PostMapping("/upload/image")
    public ResponseEntity<UploadedImage> uploadImage(@RequestParam("file") MultipartFile file) {
        log.debug("REST request to upload an image: {}", file.getOriginalFilename());

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(null); // Handle empty file upload
        }

        String fileName = file.getOriginalFilename();
        if (!fileName.toLowerCase().matches(".*\\.(png|jpg|jpeg)$")) {
            return ResponseEntity.badRequest().body(null); // Only allow certain file types
        }

        try {
            // Create directory if it doesn't exist
            File directory = new File(System.getProperty("user.dir") + "/Uploads");
            if (!directory.exists() && !directory.mkdirs()) {
                throw new IOException("Failed to create upload directory");
            }

            // Generate a unique name for the file
            String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
            Path filePath = Paths.get(uploadDir, uniqueFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Generate the URL for the uploaded image
            String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/elemento-proyectos/Uploads/")
                .path(uniqueFileName)
                .toUriString();

            // Get image dimensions
            BufferedImage image = ImageIO.read(file.getInputStream());
            int width = image.getWidth();
            int height = image.getHeight();

            // Create the UploadedImage object
            UploadedImage uploadedImage = new UploadedImage(imageUrl, fileName, width, height);

            // Return the uploaded image details in the response
            return ResponseEntity.ok(uploadedImage);
        } catch (IOException e) {
            log.error("Error uploading file", e);
            return ResponseEntity.status(500).body(null); // Internal server error in case of failure
        }
    }

    // Método auxiliar para obtener el tamaño del archivo en formato adecuado (KB, MB, etc.)
    private String getFileSize(MultipartFile file) {
        long sizeInBytes = file.getSize();
        if (sizeInBytes < 1024) {
            return sizeInBytes + "B";
        } else if (sizeInBytes < 1024 * 1024) {
            return sizeInBytes / 1024 + "KB";
        } else {
            return sizeInBytes / (1024 * 1024) + "MB";
        }
    }

    /**
     * {@code GET  /content/images/{fileName}} : Get the image file.
     *
     * @param fileName the name of the image file to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the image as a resource,
     * or with status {@code 404 (Not Found)} if the image is not found.
     */

    /**
     * {@code GET  /content/images/{fileName}} : Retrieve an image.
     *
     * Fetches an image file from the server by its file name and returns it as a
     * resource. If the file doesn't exist, returns a 404 status.
     *
     * @param fileName the name of the image file to retrieve.
     * @return the {@link ResponseEntity} containing the image as a resource, or a 404
     *         status if the file is not found.
     */
    @GetMapping("/Uploads/{fileName:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String fileName) {
        log.debug("REST request to retrieve an image: {}===========================", fileName);

        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                String contentType = Files.probeContentType(filePath);
                return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType != null ? contentType : MediaType.APPLICATION_OCTET_STREAM_VALUE))
                    .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error retrieving file: {}", fileName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
