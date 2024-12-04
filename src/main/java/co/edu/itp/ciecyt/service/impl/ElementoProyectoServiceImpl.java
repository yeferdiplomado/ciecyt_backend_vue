package co.edu.itp.ciecyt.service.impl;

import co.edu.itp.ciecyt.domain.ElementoProyecto;
import co.edu.itp.ciecyt.repository.ElementoProyectoRepository;
import co.edu.itp.ciecyt.service.ElementoProyectoService;
import co.edu.itp.ciecyt.service.dto.ElementoProyectoDTO;
import co.edu.itp.ciecyt.service.mapper.ElementoProyectoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link co.edu.itp.ciecyt.domain.ElementoProyecto}.
 */
@Service
@Transactional
public class ElementoProyectoServiceImpl implements ElementoProyectoService {

    private static final Logger log = LoggerFactory.getLogger(ElementoProyectoServiceImpl.class);

    private final ElementoProyectoRepository elementoProyectoRepository;

    private final ElementoProyectoMapper elementoProyectoMapper;

    public ElementoProyectoServiceImpl(
        ElementoProyectoRepository elementoProyectoRepository,
        ElementoProyectoMapper elementoProyectoMapper
    ) {
        this.elementoProyectoRepository = elementoProyectoRepository;
        this.elementoProyectoMapper = elementoProyectoMapper;
    }

    @Override
    public ElementoProyectoDTO save(ElementoProyectoDTO elementoProyectoDTO) {
        log.debug("Request to save ElementoProyecto : {}", elementoProyectoDTO);
        ElementoProyecto elementoProyecto = elementoProyectoMapper.toEntity(elementoProyectoDTO);
        elementoProyecto = elementoProyectoRepository.save(elementoProyecto);
        return elementoProyectoMapper.toDto(elementoProyecto);
    }

    @Override
    public ElementoProyectoDTO update(ElementoProyectoDTO elementoProyectoDTO) {
        log.debug("Request to update ElementoProyecto : {}", elementoProyectoDTO);
        ElementoProyecto elementoProyecto = elementoProyectoMapper.toEntity(elementoProyectoDTO);
        elementoProyecto = elementoProyectoRepository.save(elementoProyecto);
        return elementoProyectoMapper.toDto(elementoProyecto);
    }

    @Override
    public Optional<ElementoProyectoDTO> partialUpdate(ElementoProyectoDTO elementoProyectoDTO) {
        log.debug("Request to partially update ElementoProyecto : {}", elementoProyectoDTO);

        return elementoProyectoRepository
            .findById(elementoProyectoDTO.getId())
            .map(existingElementoProyecto -> {
                elementoProyectoMapper.partialUpdate(existingElementoProyecto, elementoProyectoDTO);

                return existingElementoProyecto;
            })
            .map(elementoProyectoRepository::save)
            .map(elementoProyectoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ElementoProyectoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ElementoProyectos");
        return elementoProyectoRepository.findAll(pageable).map(elementoProyectoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ElementoProyectoDTO> findOne(Long id) {
        log.debug("Request to get ElementoProyecto : {}", id);
        return elementoProyectoRepository.findById(id).map(elementoProyectoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ElementoProyecto : {}", id);
        elementoProyectoRepository.deleteById(id);
    }
}
