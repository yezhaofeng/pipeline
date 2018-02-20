package com.jlu.github.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jlu.github.model.Module;
import com.jlu.github.service.IModuleService;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by langshiquan on 17/3/21.
 */
@Controller
@RequestMapping("/pipeline/module")
public class ModuleController {

    @Autowired
    private IModuleService moduleService;

    @ApiIgnore
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    @ResponseBody
    public List<Module> getSuggetsModules(@RequestParam(value = "q", required = false) String query,
                                          @RequestParam String owner,
                                          @RequestParam Integer limit) {
        List<Module> modules = new ArrayList<Module>();
        if (StringUtils.isNotBlank(query)) {
            String pathStr = new String(query);
            modules = moduleService.getSuggestProductModules(pathStr, owner, limit);
        }
        if (null == modules) {
            modules = new ArrayList<>();
        }
        Collections.sort(modules, new Comparator<Module>() {
            @Override
            public int compare(Module o1, Module o2) {
                return o1.getModule().compareTo(o2.getModule());
            }
        });
        return modules;
    }
}
