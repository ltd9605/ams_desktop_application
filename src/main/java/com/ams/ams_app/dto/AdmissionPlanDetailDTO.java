package com.ams.ams_app.dto;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdmissionPlanDetailDTO {
    private AdmissionPlanDTO planInfor;
    private ArrayList<CombinationDTO> combinations;
    private ArrayList<SubCriteriaDTO> subCriterias;

    public AdmissionPlanDetailDTO(AdmissionPlanDTO planInfor, ArrayList<CombinationDTO> combinations, ArrayList<SubCriteriaDTO> subCriterias) {
        this.planInfor = planInfor;
        this.combinations = combinations;
        this.subCriterias = subCriterias;
    }

    public AdmissionPlanDetailDTO() {

    }

    public AdmissionPlanDTO getPlanInfor() {
        return planInfor;
    }

    public void setPlanInfor(AdmissionPlanDTO planInfor) {
        this.planInfor = planInfor;
    }

    @Override
    public String toString() {
        return "AdmissionPlanDetailDTO{" +
                "planInfor=" + planInfor +
                ", combinations=" + combinations +
                ", subCriterias=" + subCriterias +
                '}';
    }

    public ArrayList<SubCriteriaDTO> getSubCriterias() {
        return subCriterias;
    }

    public void setSubCriterias(ArrayList<SubCriteriaDTO> subCriterias) {
        this.subCriterias = subCriterias;
    }

    public ArrayList<CombinationDTO> getCombinations() {
        return combinations;
    }

    public void setCombinations(ArrayList<CombinationDTO> combinations) {
        this.combinations = combinations;
    }
}
