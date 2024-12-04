package co.edu.itp.ciecyt.web.rest;

import co.edu.itp.ciecyt.repository.ElementoRepository;
import co.edu.itp.ciecyt.service.ElementoService;
import co.edu.itp.ciecyt.service.dto.ElementoDTO;
import co.edu.itp.ciecyt.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link co.edu.itp.ciecyt.domain.Elemento}.
 */
@RestController
@RequestMapping("/api/elementos")
public class ElementoResource {

    private static final Logger log = LoggerFactory.getLogger(ElementoResource.class);

    private static final String ENTITY_NAME = "elemento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ElementoService elementoService;

    private final ElementoRepository elementoRepository;

    public ElementoResource(ElementoService elementoService, ElementoRepository elementoRepository) {
        this.elementoService = elementoService;
        this.elementoRepository = elementoRepository;
    }

    /**
     * {@code POST  /elementos} : Create a new elemento.
     *
     * @param elementoDTO the elementoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new elementoDTO, or with status {@code 400 (Bad Request)} if the elemento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ElementoDTO> createElemento(@Valid @RequestBody ElementoDTO elementoDTO) throws URISyntaxException {
        log.debug("REST request to save Elemento : {}", elementoDTO);
        if (elementoDTO.getId() != null) {
            throw new BadRequestAlertException("A new elemento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        elementoDTO = elementoService.save(elementoDTO);
        return ResponseEntity.created(new URI("/api/elementos/" + elementoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, elementoDTO.getId().toString()))
            .body(elementoDTO);
    }

    /**
     * {@code PUT  /elementos/:id} : Updates an existing elemento.
     *
     * @param id the id of the elementoDTO to save.
     * @param elementoDTO the elementoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated elementoDTO,
     * or with status {@code 400 (Bad Request)} if the elementoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the elementoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ElementoDTO> updateElemento(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ElementoDTO elementoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Elemento : {}, {}", id, elementoDTO);
        if (elementoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, elementoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!elementoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        elementoDTO = elementoService.update(elementoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, elementoDTO.getId().toString()))
            .body(elementoDTO);
    }

    /**
     * {@code PATCH  /elementos/:id} : Partial updates given fields of an existing elemento, field will ignore if it is null
     *
     * @param id the id of the elementoDTO to save.
     * @param elementoDTO the elementoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated elementoDTO,
     * or with status {@code 400 (Bad Request)} if the elementoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the elementoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the elementoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ElementoDTO> partialUpdateElemento(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ElementoDTO elementoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Elemento partially : {}, {}", id, elementoDTO);
        if (elementoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, elementoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!elementoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ElementoDTO> result = elementoService.partialUpdate(elementoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, elementoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /elementos} : get all the elementos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of elementos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ElementoDTO>> getAllElementos(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Elementos");
        Page<ElementoDTO> page = elementoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /elementos/:id} : get the "id" elemento.
     *
     * @param id the id of the elementoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the elementoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ElementoDTO> getElemento(@PathVariable("id") Long id) {
        log.debug("REST request to get Elemento : {}", id);
        Optional<ElementoDTO> elementoDTO = elementoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(elementoDTO);
    }

    /**
     * {@code DELETE  /elementos/:id} : delete the "id" elemento.
     *
     * @param id the id of the elementoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteElemento(@PathVariable("id") Long id) {
        log.debug("REST request to delete Elemento : {}", id);
        elementoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
