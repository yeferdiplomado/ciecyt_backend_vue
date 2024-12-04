package co.edu.itp.ciecyt.web.rest;

import co.edu.itp.ciecyt.repository.ProyectoRepository;
import co.edu.itp.ciecyt.service.ProyectoService;
import co.edu.itp.ciecyt.service.dto.ProyectoDTO;
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
 * REST controller for managing {@link co.edu.itp.ciecyt.domain.Proyecto}.
 */
@RestController
@RequestMapping("/api/proyectos")
public class ProyectoResource {

    private static final Logger log = LoggerFactory.getLogger(ProyectoResource.class);

    private static final String ENTITY_NAME = "proyecto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProyectoService proyectoService;

    private final ProyectoRepository proyectoRepository;

    public ProyectoResource(ProyectoService proyectoService, ProyectoRepository proyectoRepository) {
        this.proyectoService = proyectoService;
        this.proyectoRepository = proyectoRepository;
    }

    /**
     * {@code POST  /proyectos} : Create a new proyecto.
     *
     * @param proyectoDTO the proyectoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new proyectoDTO, or with status {@code 400 (Bad Request)} if the proyecto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProyectoDTO> createProyecto(@Valid @RequestBody ProyectoDTO proyectoDTO) throws URISyntaxException {
        log.debug("REST request to save Proyecto : {}", proyectoDTO);
        if (proyectoDTO.getId() != null) {
            throw new BadRequestAlertException("A new proyecto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        proyectoDTO = proyectoService.save(proyectoDTO);
        return ResponseEntity.created(new URI("/api/proyectos/" + proyectoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, proyectoDTO.getId().toString()))
            .body(proyectoDTO);
    }

    /**
     * {@code PUT  /proyectos/:id} : Updates an existing proyecto.
     *
     * @param id the id of the proyectoDTO to save.
     * @param proyectoDTO the proyectoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated proyectoDTO,
     * or with status {@code 400 (Bad Request)} if the proyectoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the proyectoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProyectoDTO> updateProyecto(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProyectoDTO proyectoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Proyecto : {}, {}", id, proyectoDTO);
        if (proyectoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, proyectoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!proyectoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        proyectoDTO = proyectoService.update(proyectoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, proyectoDTO.getId().toString()))
            .body(proyectoDTO);
    }

    /**
     * {@code PATCH  /proyectos/:id} : Partial updates given fields of an existing proyecto, field will ignore if it is null
     *
     * @param id the id of the proyectoDTO to save.
     * @param proyectoDTO the proyectoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated proyectoDTO,
     * or with status {@code 400 (Bad Request)} if the proyectoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the proyectoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the proyectoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProyectoDTO> partialUpdateProyecto(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProyectoDTO proyectoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Proyecto partially : {}, {}", id, proyectoDTO);
        if (proyectoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, proyectoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!proyectoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProyectoDTO> result = proyectoService.partialUpdate(proyectoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, proyectoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /proyectos} : get all the proyectos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of proyectos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProyectoDTO>> getAllProyectos(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Proyectos");
        Page<ProyectoDTO> page = proyectoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /proyectos/:id} : get the "id" proyecto.
     *
     * @param id the id of the proyectoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the proyectoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProyectoDTO> getProyecto(@PathVariable("id") Long id) {
        log.debug("REST request to get Proyecto : {}", id);
        Optional<ProyectoDTO> proyectoDTO = proyectoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(proyectoDTO);
    }

    /**
     * {@code DELETE  /proyectos/:id} : delete the "id" proyecto.
     *
     * @param id the id of the proyectoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProyecto(@PathVariable("id") Long id) {
        log.debug("REST request to delete Proyecto : {}", id);
        proyectoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
