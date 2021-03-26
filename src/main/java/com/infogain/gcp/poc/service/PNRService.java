package com.infogain.gcp.poc.service;

import com.infogain.gcp.poc.entity.PNREntity;
import com.infogain.gcp.poc.entity.PNROutBoxEntity;
import com.infogain.gcp.poc.model.PNRModel;
import com.infogain.gcp.poc.repository.PNROutBoxRepository;
import com.infogain.gcp.poc.repository.PNRRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PNRService {

    @Autowired
    private PNRRepository pnrRepository;

    @Autowired
    private PNROutBoxRepository pnrOutBoxRepository;

    private PNREntity savePNREntity(PNREntity pnrEntity){
        // TODO validae for null pnrEntity
        return pnrRepository.save(pnrEntity);
    }

    private PNROutBoxEntity savePNROutBoxEntity(PNROutBoxEntity pnrOutBoxEntity){
        // TODO validate for null PNROutBoxEntity
        return pnrOutBoxRepository.save(pnrOutBoxEntity);
    }

    @SuppressWarnings("all")
    @Transactional
    public PNRModel savePNRModel(PNRModel pnrModel){
        // TODO validate for null pnrModel

        PNREntity pnrEntity = pnrModel.buildEntity();

        // save PNREntity
        log.info("Saving PNREntity={}", pnrEntity);
        PNREntity persistedPNREntity = savePNREntity(pnrEntity);
        log.info("Saved PNREntity={}", persistedPNREntity);

        // save PNROutBoxEntity
        PNROutBoxEntity pnrOutBoxEntity = PNROutBoxEntity.builder().pnrId(persistedPNREntity.getPnrId()).isProcessed(false).retryCount(0).build();
        log.info("Saving PNROutBoxEntity={}", pnrOutBoxEntity);
        PNROutBoxEntity persistedPNROutBoxEntity = savePNROutBoxEntity(pnrOutBoxEntity);
        log.info("Saved PNROutBoxEntity={}", persistedPNROutBoxEntity);

        PNRModel resultPNRModel = persistedPNREntity.buildModel();

        // TODO Make async call to pub-sub-publisher

        return resultPNRModel;
    }

    @SuppressWarnings("all")
    @Transactional
    public PNRModel updatePNRModel(PNRModel pnrModel){
        // TODO validate for null pnrModel

        String pnrId = pnrModel.getPnrId();
        PNREntity pnrEntity = pnrRepository.findById(pnrId).get(); // TODO throw exception is object not found before calling get()
        pnrModel.updateEntity(pnrEntity);

        // save PNREntity
        log.info("Updating PNREntity={}", pnrEntity);
        PNREntity persistedPNREntity = savePNREntity(pnrEntity);
        log.info("Updated PNREntity={}", persistedPNREntity);

        // save PNROutBoxEntity
        PNROutBoxEntity pnrOutBoxEntity = PNROutBoxEntity.builder().pnrId(persistedPNREntity.getPnrId()).isProcessed(false).retryCount(0).build();
        log.info("Updating PNROutBoxEntity={}", pnrOutBoxEntity);
        PNROutBoxEntity persistedPNROutBoxEntity = savePNROutBoxEntity(pnrOutBoxEntity);
        log.info("Updated PNROutBoxEntity={}", persistedPNROutBoxEntity);

        PNRModel resultPNRModel = persistedPNREntity.buildModel();

        // TODO Make async call to pub-sub-publisher

        return resultPNRModel;
    }

    public List<PNRModel> findAllPNRModelList(){
        log.info("find all PNREntity objects");
        Iterable<PNREntity> pnrEntityIterable = pnrRepository.findAll();
        List<PNREntity> pnrEntityList = IterableUtils.toList(pnrEntityIterable);
        return pnrEntityList.stream().map(PNREntity::buildModel).collect(Collectors.toList());
    }

}