package co.edu.itp.ciecyt.service.impl;

import co.edu.itp.ciecyt.domain.Elemento;
import co.edu.itp.ciecyt.repository.ElementoRepository;
import co.edu.itp.ciecyt.service.ElementoService;
import co.edu.itp.ciecyt.service.dto.ElementoDTO;
import co.edu.itp.ciecyt.service.mapper.ElementoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link co.edu.itp.ciecyt.domain.Elemento}.
 */
@Service
@Transactional
public class ElementoServiceImpl implements ElementoService {

    private static final Logger log = LoggerFactory.getLogger(ElementoServiceImpl.class);

    private final ElementoRepository elementoRepository;

    private final ElementoMapper elementoMapper;

    public ElementoServiceImpl(ElementoRepository elementoRepository, ElementoMapper elementoMapper) {
        this.elementoRepository = elementoRepository;
        this.elementoMapper = elementoMapper;
    }

    @Override
    public ElementoDTO save(ElementoDTO elementoDTO) {
        log.debug("Request to save Elemento : {}", elementoDTO);
        Elemento elemento = elementoMapper.toEntity(elementoDTO);
        elemento = elementoRepository.save(elemento);
        return elementoMapper.toDto(elemento);
    }

    @Override
    public ElementoDTO update(ElementoDTO elementoDTO) {
        log.debug("Request to update Elemento : {}", elementoDTO);
        Elemento elemento = elementoMapper.toEntity(elementoDTO);
        elemento = elementoRepository.save(elemento);
        return elementoMapper.toDto(elemento);
    }

    @Override
    public Optional<ElementoDTO> partialUpdate(ElementoDTO elementoDTO) {
        log.debug("Request to partially update Elemento : {}", elementoDTO);

        return elementoRepository
            .findById(elementoDTO.getId())
            .map(existingElemento -> {
                elementoMapper.partialUpdate(existingElemento, elementoDTO);

                return existingElemento;
            })
            .map(elementoRepository::save)
            .map(elementoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ElementoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Elementos");
        return elementoRepository.findAll(pageable).map(elementoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ElementoDTO> findOne(Long id) {
        log.debug("Request to get Elemento : {}", id);
        return elementoRepository.findById(id).map(elementoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Elemento : {}", id);
        elementoRepository.deleteById(id);
    }
}
