package com.infogain.gcp.poc.service;

import com.infogain.gcp.poc.component.RestClientUtil;
import com.infogain.gcp.poc.entity.PNREntity;
import com.infogain.gcp.poc.entity.PNROutBoxEntity;
import com.infogain.gcp.poc.model.PNRModel;
import com.infogain.gcp.poc.repository.PNROutBoxRepository;
import com.infogain.gcp.poc.repository.PNRRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PNRService {

    @Value("${app.publish.end.point}")
    private String publishEndPoint;

    @Value("${app.publish.base.url}")
    private String baseUrl;

    @Autowired
    private PNRRepository pnrRepository;

    @Autowired
    private PNROutBoxRepository pnrOutBoxRepository;

    @Autowired
    private RestClientUtil restClientUtil;

    private PNREntity savePNREntity(PNREntity pnrEntity){
        // TODO validae for null pnrEntity
        return pnrRepository.save(pnrEntity);
    }

    private PNROutBoxEntity savePNROutBoxEntity(PNROutBoxEntity pnrOutBoxEntity){
        // TODO validate for null PNROutBoxEntity
        return pnrOutBoxRepository.save(pnrOutBoxEntity);
    }

    private void publishRecordChanges(PNRModel resultPNRModel) {
        WebClient webClient = WebClient.create(baseUrl);
        log.info("Calling publish API");
        Mono<String> resultMono = webClient.post().uri(publishEndPoint).body(Mono.just(resultPNRModel), PNRModel.class).retrieve().bodyToMono(String.class);
        resultMono.subscribe(System.out::println);
        log.info("Called publish API");
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
        // restClientUtil.callPostAPI(resultPNRModel, publishEndPoint);

        publishRecordChanges(resultPNRModel);

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
        publishRecordChanges(resultPNRModel);

        return resultPNRModel;
    }

    public List<PNRModel> findAllPNRModelList(){
        log.info("find all PNREntity objects");
        Iterable<PNREntity> pnrEntityIterable = pnrRepository.findAll();
        List<PNREntity> pnrEntityList = IterableUtils.toList(pnrEntityIterable);
        return pnrEntityList.stream().map(PNREntity::buildModel).collect(Collectors.toList());
    }

}