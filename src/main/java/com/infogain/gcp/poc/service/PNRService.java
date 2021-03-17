package com.infogain.gcp.poc.service;

import com.infogain.gcp.poc.entity.PNREntity;
import com.infogain.gcp.poc.model.PNRModel;
import com.infogain.gcp.poc.repository.PNRRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PNRService {

    @Autowired
    private PNRRepository pnrRepository;

    private PNREntity savePNREntity(PNREntity pnrEntity){
        // TODO validae for null pnrEntity
        return pnrRepository.save(pnrEntity);
    }

    private List<PNREntity> savePNREntityList(List<PNREntity>  pnrEntityList){
        if(CollectionUtils.isEmpty(pnrEntityList)){
            return Collections.emptyList();
        }

        Iterable<PNREntity> pnrEntityIterable = pnrRepository.saveAll(pnrEntityList);
        return IterableUtils.toList(pnrEntityIterable);
    }

    public PNRModel saveOrUpdatePNRModel(PNRModel pnrModel){
        // TODO validate for null pnrModel

        String pnrId = pnrModel.getPnrId();
        PNREntity pnrEntity = null;

        if(StringUtils.isBlank(pnrId)){
            // save
            pnrEntity = pnrModel.buildEntity();
            pnrEntity.setPnrId(UUID.randomUUID().toString());
        }else{
            // update
            pnrEntity = pnrRepository.findById(pnrId).get(); // TODO throw exception is object not found before calling get()
            pnrModel.updateEntity(pnrEntity);
        }

        PNREntity persistedPNREntity = savePNREntity(pnrEntity);
        return persistedPNREntity.buildModel();
    }

    public List<PNRModel> saveOrUpdatePNRModelList(List<PNRModel> pnrModelList){
        if(CollectionUtils.isEmpty(pnrModelList)){
            return Collections.emptyList();
        }

        List<PNREntity> pnrEntityList = pnrModelList.stream().map(pnrModel -> {
            String pnrId = pnrModel.getPnrId();
            PNREntity pnrEntity = null;

            if(StringUtils.isBlank(pnrId)){
                // save
                pnrEntity = pnrModel.buildEntity();
            }else{
                // update
                pnrEntity = pnrRepository.findById(pnrId).get(); // TODO throw exception is object not found before calling get()
                pnrModel.updateEntity(pnrEntity);
            }
            return pnrEntity;
        }).collect(Collectors.toList());

        List<PNREntity> persistedPNREntityList = savePNREntityList(pnrEntityList);
        return persistedPNREntityList.stream().map(PNREntity::buildModel).collect(Collectors.toList());
    }

    public List<PNRModel> findAllPNRModelList(){
        Iterable<PNREntity> pnrEntityIterable = pnrRepository.findAll();
        List<PNREntity> pnrEntityList = IterableUtils.toList(pnrEntityIterable);
        return pnrEntityList.stream().map(PNREntity::buildModel).collect(Collectors.toList());
    }

}