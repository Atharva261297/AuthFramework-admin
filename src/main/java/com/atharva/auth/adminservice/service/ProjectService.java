package com.atharva.auth.adminservice.service;

import com.atharva.auth.adminservice.client.ProjectFeignClient;
import com.atharva.auth.adminservice.dao.AdminRepository;
import com.atharva.auth.adminservice.dao.ProjectRepository;
import com.atharva.auth.adminservice.dao.RelationRepository;
import com.atharva.auth.adminservice.model.*;
import com.atharva.auth.adminservice.model.ui.ProjectAdminsModel;
import com.atharva.auth.adminservice.model.ui.ProjectDetailModel;
import com.atharva.auth.adminservice.model.ui.ProjectUiModel;
import com.atharva.auth.adminservice.utils.constants.ErrorCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@EnableTransactionManagement
public class ProjectService {

    @Autowired
    private ProjectFeignClient client;

    @Autowired
    private ProjectRepository projectDao;

    @Autowired
    private RelationRepository relationDao;

    @Autowired
    private AdminRepository adminDao;

    public ErrorCodes registerProject(ProjectModel projectModel) {
        String id = Base64.getEncoder().encodeToString(projectModel.getId().getBytes());
        String pass = Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes());
        ErrorCodes code = client.registerProject(id + ":" + pass);
        if (code == ErrorCodes.SUCCESS) {
            projectDao.save(projectModel);
            return ErrorCodes.SUCCESS;
        } else {
            return code;
        }
    }

    public List<ProjectModel> getProjectList(String userId) {
        List<ProjectModel> owner = projectDao.findAll(Example.of(new ProjectModel(null, null, userId, null)));
        List<ProjectAdminRelation> writeProjects = relationDao.findAll(Example.of(new ProjectAdminRelation(null, userId, AdminRights.WRITE)));
        List<ProjectAdminRelation> readProjects = relationDao.findAll(Example.of(new ProjectAdminRelation(null, userId, AdminRights.READ)));
        writeProjects.forEach(r -> {
            owner.add(projectDao.getOne(r.getProjectId()));
        });
        readProjects.forEach(r -> {
            owner.add(projectDao.getOne(r.getProjectId()));
        });
        return owner;
    }

    public List<ProjectUiModel> getProjectUiList(String userId) {
        List<ProjectModel> projectList = getProjectList(userId);
        List<ProjectUiModel> projectUiModelList = new ArrayList<>();
        projectList.forEach(prj -> {
            long countWrite = relationDao.count(Example.of(new ProjectAdminRelation(prj.getId(), null, AdminRights.WRITE)));
            long countRead = relationDao.count(Example.of(new ProjectAdminRelation(prj.getId(), null, AdminRights.READ)));
            projectUiModelList.add(new ProjectUiModel(prj, Math.toIntExact(countWrite + countRead)));
        });
        return projectUiModelList;
    }

    public ProjectDetailModel getProjectDetails(String projectId, String userId) {
        ProjectModel prj = projectDao.getOne(projectId);

        List<ProjectAdminRelation> readAdmins = relationDao.findAll(Example.of(new ProjectAdminRelation(projectId, null, AdminRights.READ)));
        List<ProjectAdminRelation> writeAdmins = relationDao.findAll(Example.of(new ProjectAdminRelation(projectId, null, AdminRights.WRITE)));
        List<ProjectAdminsModel> admins = new ArrayList<>();

        readAdmins.forEach(rAd -> {
            admins.add(new ProjectAdminsModel(rAd.getAdminId(), adminDao.getOne(rAd.getAdminId()).getName(), rAd.getRights()));
        });
        writeAdmins.forEach(wAd -> {
            admins.add(new ProjectAdminsModel(wAd.getAdminId(), adminDao.getOne(wAd.getAdminId()).getName(), wAd.getRights()));
        });

        AdminModel owner = adminDao.getOne(prj.getOwnerId());

        String key = client.getCreds(projectId);

        return new ProjectDetailModel(
                projectId,
                prj.getName(),
                owner,
                key,
                prj.getUserSize(),
                admins,
                userId.equals(owner.getId()),
                writeAdmins.contains(new ProjectAdminRelation(projectId, userId, AdminRights.WRITE)) || userId.equals(owner.getId())
        );
    }

    @Transactional
    public void updateProject(ProjectModel updatedPrj) {
        ProjectModel fromDb = projectDao.getOne(updatedPrj.getId());
        fromDb.setOwnerId(updatedPrj.getOwnerId());
        fromDb.setName(updatedPrj.getName());
        projectDao.save(fromDb);
    }

    @Transactional
    public void addAdmin(ProjectAdminsModel newAdmin, String projectId) {
        relationDao.save(new ProjectAdminRelation(projectId, newAdmin.getId(), newAdmin.getRights()));
    }

    @Transactional
    public void updateAdmin(String adminId, String projectId, AdminRights rights) {
        ProjectAdminRelation fromDb = relationDao.getOne(new RelationId(projectId, adminId));
        fromDb.setRights(rights);
        relationDao.save(fromDb);
    }

    @Transactional
    public void deleteAdmin(String adminId, String projectId) {
        ProjectAdminRelation fromDb = relationDao.getOne(new RelationId(projectId, adminId));
        relationDao.delete(fromDb);
    }
}
