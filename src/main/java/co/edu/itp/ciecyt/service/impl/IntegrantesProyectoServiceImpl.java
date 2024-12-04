package co.edu.itp.ciecyt.service.impl;

import co.edu.itp.ciecyt.domain.IntegrantesProyecto;
import co.edu.itp.ciecyt.repository.IntegrantesProyectoRepository;
import co.edu.itp.ciecyt.service.IntegrantesProyectoService;
import co.edu.itp.ciecyt.service.dto.IntegrantesProyectoDTO;
import co.edu.itp.ciecyt.service.mapper.IntegrantesProyectoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link co.edu.itp.ciecyt.domain.IntegrantesProyecto}.
 */
@Service
@Transactional
public class IntegrantesProyectoServiceImpl implements IntegrantesProyectoService {

    private static final Logger log = LoggerFactory.getLogger(IntegrantesProyectoServiceImpl.class);

    private final IntegrantesProyectoRepository integrantesProyectoRepository;

    private final IntegrantesProyectoMapper integrantesProyectoMapper;

    public IntegrantesProyectoServiceImpl(
        IntegrantesProyectoRepository integrantesProyectoRepository,
        IntegrantesProyectoMapper integrantesProyectoMapper
    ) {
        this.integrantesProyectoRepository = integrantesProyectoRepository;
        this.integrantesProyectoMapper = integrantesProyectoMapper;
    }

    @Override
    public IntegrantesProyectoDTO save(IntegrantesProyectoDTO integrantesProyectoDTO) {
        log.debug("Request to save IntegrantesProyecto : {}", integrantesProyectoDTO);
        IntegrantesProyecto integrantesProyecto = integrantesProyectoMapper.toEntity(integrantesProyectoDTO);
        integrantesProyecto = integrantesProyectoRepository.save(integrantesProyecto);
        return integrantesProyectoMapper.toDto(integrantesProyecto);
    }

    @Override
    public IntegrantesProyectoDTO update(IntegrantesProyectoDTO integrantesProyectoDTO) {
        log.debug("Request to update IntegrantesProyecto : {}", integrantesProyectoDTO);
        IntegrantesProyecto integrantesProyecto = integrantesProyectoMapper.toEntity(integrantesProyectoDTO);
        integrantesProyecto = integrantesProyectoRepository.save(integrantesProyecto);
        return integrantesProyectoMapper.toDto(integrantesProyecto);
    }

    @Override
    public Optional<IntegrantesProyectoDTO> partialUpdate(IntegrantesProyectoDTO integrantesProyectoDTO) {
        log.debug("Request to partially update IntegrantesProyecto : {}", integrantesProyectoDTO);

        return integrantesProyectoRepository
            .findById(integrantesProyectoDTO.getId())
            .map(existingIntegrantesProyecto -> {
                integrantesProyectoMapper.partialUpdate(existingIntegrantesProyecto, integrantesProyectoDTO);

                return existingIntegrantesProyecto;
            })
            .map(integrantesProyectoRepository::save)
            .map(integrantesProyectoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IntegrantesProyectoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all IntegrantesProyectos");
        return integrantesProyectoRepository.findAll(pageable).map(integrantesProyectoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IntegrantesProyectoDTO> findOne(Long id) {
        log.debug("Request to get IntegrantesProyecto : {}", id);
        return integrantesProyectoRepository.findById(id).map(integrantesProyectoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete IntegrantesProyecto : {}", id);
        integrantesProyectoRepository.deleteById(id);
    }
}
