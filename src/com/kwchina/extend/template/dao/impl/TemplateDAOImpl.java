package com.kwchina.extend.template.dao.impl;

import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.common.dao.BasicDaoImpl;
import com.kwchina.extend.template.dao.TemplateDAO;
import com.kwchina.extend.template.entity.ZhaotouTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TemplateDAOImpl extends BasicDaoImpl<ZhaotouTemplate> implements TemplateDAO {

    @Override
    public ZhaotouTemplate findByName(String name) {
        String queryString = "from ZhaotouTemplate template where template.templateName = '" + name + "'";
        List list = this.getResultByQueryString(queryString);

        if (list != null && list.size() > 0 && list.get(0) != null) {
            return (ZhaotouTemplate) list.get(0);
        } else {
            return null;
        }
    }
}