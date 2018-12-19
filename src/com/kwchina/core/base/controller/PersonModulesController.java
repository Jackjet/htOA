package com.kwchina.core.base.controller;

import com.kwchina.core.base.entity.*;
import com.kwchina.core.base.service.ModulesManager;
import com.kwchina.core.base.service.PersonInforManager;
import com.kwchina.core.base.service.PersonModulesManager;
import com.kwchina.core.base.vo.PersonModuleVo;
import com.kwchina.core.util.Json;
import com.kwchina.oa.util.SysCommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Create by yuanjl on 2018/1/4
 */
@Controller
@RequestMapping(value="/core/personModules.do")
public class PersonModulesController {
    @Autowired
    private PersonModulesManager personModulesManager;
    @Autowired
    private ModulesManager modulesManager;

    @Autowired
    private PersonInforManager personInforManager;

    @RequestMapping(params="method=editPersonModules")
    public String editModules(HttpServletRequest request, HttpServletResponse response){
        SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
        if(systemUser==null){
            return "error";
        }
        PersonModules personModules = personModulesManager.selectByPerson(systemUser.getPersonId());
        request.setAttribute("_PersonModules",personModules);
        return "editModules";
    }

    @ResponseBody
    @RequestMapping(params="method=savePersonModules")
    public Json savePersonModules(HttpServletRequest request, HttpServletResponse response, PersonModuleVo personModuleVo){
        Json json = new Json();
        SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
        if(systemUser==null || personModuleVo==null){
            return json;
        }
        PersonModules personModules =  personModulesManager.selectByPerson(systemUser.getPersonId());
        if(personModules==null){
            personModules = new PersonModules();
        }
        try{
            PersonInfor personInfor = (PersonInfor) personInforManager.get(systemUser.getPersonId());

            Modules module1 = modulesManager.getByName(personModuleVo.getName1());
            Modules module2 = modulesManager.getByName(personModuleVo.getName2());
            Modules module3 = modulesManager.getByName(personModuleVo.getName3());
            Modules module4 = modulesManager.getByName(personModuleVo.getName4());
            Modules module5 = modulesManager.getByName(personModuleVo.getName5());
            Modules module6 = modulesManager.getByName(personModuleVo.getName6());

            personModules.setPersonInfor(personInfor);

            personModules.setName1(personModuleVo.getName1());
            personModules.setName2(personModuleVo.getName2());
            personModules.setName3(personModuleVo.getName3());
            personModules.setName4(personModuleVo.getName4());
            personModules.setName5(personModuleVo.getName5());
            personModules.setName6(personModuleVo.getName6());

            personModules.setColor1(personModuleVo.getColor1());
            personModules.setColor2(personModuleVo.getColor2());
            personModules.setColor3(personModuleVo.getColor3());
            personModules.setColor4(personModuleVo.getColor4());
            personModules.setColor5(personModuleVo.getColor5());
            personModules.setColor6(personModuleVo.getColor6());

            personModules.setMoreUrl1(module1.getMore());
            personModules.setMoreUrl2(module2.getMore());
            personModules.setMoreUrl3(module3.getMore());
            personModules.setMoreUrl4(module4.getMore());
            personModules.setMoreUrl5(module5.getMore());
            personModules.setMoreUrl6(module6.getMore());

            personModules.setUlId1(module1.getUrl());
            personModules.setUlId2(module2.getUrl());
            personModules.setUlId3(module3.getUrl());
            personModules.setUlId4(module4.getUrl());
            personModules.setUlId5(module5.getUrl());
            personModules.setUlId6(module6.getUrl());



            personModulesManager.saveModule(personModules);
            json.setSuccess(true);
            return json;

        }catch (Exception e){
            System.out.println("修改模块失败");
            json.setSuccess(false);
        }

        return json;
    }


}
