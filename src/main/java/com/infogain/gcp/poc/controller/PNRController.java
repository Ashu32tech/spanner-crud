package com.infogain.gcp.poc.controller;

import com.infogain.gcp.poc.model.PNRModel;
import com.infogain.gcp.poc.service.PNRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class PNRController {

    @Autowired
    private PNRService pnrService;

    @PostMapping(value = "/v1/pnrs", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PNRModel savePNRModel(@RequestBody PNRModel pnrModel){
        return pnrService.saveOrUpdatePNRModel(pnrModel);
    }

    @PutMapping(value = "/v1/pnrs", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PNRModel updatePNRModel(@RequestBody PNRModel pnrModel){
        return pnrService.saveOrUpdatePNRModel(pnrModel);
    }

    @PostMapping(value = "/v2/pnrs", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PNRModel> savePNRModelList(@RequestBody List<PNRModel> pnrModelList){
        return pnrService.saveOrUpdatePNRModelList(pnrModelList);
    }

    @PutMapping(value = "/v2/pnrs", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PNRModel> updatePNRModelList(@RequestBody List<PNRModel> pnrModelList){
        return pnrService.saveOrUpdatePNRModelList(pnrModelList);
    }

    @GetMapping(value = "/v1/pnrs", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PNRModel> findAllPNRModelList(){
        return pnrService.findAllPNRModelList();
    }

}