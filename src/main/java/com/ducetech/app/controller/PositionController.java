package com.ducetech.app.controller;


import com.alibaba.fastjson.JSONObject;
import com.ducetech.app.model.Position;
import com.ducetech.app.model.User;
import com.ducetech.app.service.PositionService;
import com.ducetech.framework.controller.BaseController;
import com.ducetech.framework.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;


@Controller
public class PositionController extends BaseController {
	protected Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private PositionService positionService;

	/**
	 * @Title: person
	 * @param model
	 * @return String
	 * @Description: 跳转部门首页
	 */
	@RequestMapping(value = "/position", method = RequestMethod.GET)
	public String positions(ModelMap model) {
        List<Position> stationArea = positionService.selectByParentCode("000",6);
        System.out.println(stationArea.size()+"||||||");
        model.put("stationArea",stationArea);
	    return "/position/index";
	}

	/**
	 * @Title: personData
	 * @return void
	 * @throws Exception
	 * @Description: 部门首页数据
	 */
	@RequestMapping(value = "/positions", method = RequestMethod.GET)
	@ResponseBody
	public List<Position> positionData(HttpServletRequest request) throws Exception {
		List<Position> stationArea = positionService.selectByParentCode("000",6);
		System.out.println(stationArea.size()+"||||||");
		return stationArea;
	}

    /**
     * @Title: addPosition
     * @return void
     * @Description: 新增站区
     */
    @RequestMapping(value = "/position/addStationAreaForm", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject addStationAreaForm(Position position, String parentCode, HttpServletRequest request) throws Exception {
        User userInfo = getLoginUser(request);
        position.setCreatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
        position.setCreatorId(userInfo.getUserId());
        position.setIfUse(0);
        if(parentCode==null) {
            position.setPositionCode(positionService.selectPositionByParentCode("000"));
        }else{
            position.setPositionCode(positionService.selectPositionByParentCode(parentCode));
        }
        positionService.insertPosition(position);
        JSONObject obj = new JSONObject();
        obj.put("msg","新增站区成功");
        return obj;
    }
    /**
     * @Description: 更新站区
     */
    @RequestMapping(value = "/position/editStationAreaForm", method = RequestMethod.PUT)
    @ResponseBody
    public JSONObject editStationAreaForm(Position position, HttpServletRequest request) throws Exception {
        System.out.println(position.getPositionName()+"|"+new String(position.getPositionName().getBytes("ISO-8859-1"),"utf-8"));
        position.setPositionName(new String(position.getPositionName().getBytes("ISO-8859-1"),"utf-8"));
        User userInfo = getLoginUser(request);
        position.setUpdatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
        position.setUpdatorId(userInfo.getUserId());
        positionService.updatePosition(position);
        JSONObject obj = new JSONObject();
        obj.put("msg"," 更新站区成功");
        return obj;
    }

    /**
     * 添加站点
     * @param position
     * @param parentCode
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/position/addStationForm", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject addStationForm(Position position, String parentCode, HttpServletRequest request) throws Exception {
        User userInfo = getLoginUser(request);
        position.setCreatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
        position.setCreatorId(userInfo.getUserId());
        position.setIfUse(0);
        if(parentCode==null) {
            position.setPositionCode(positionService.selectPositionByParentCode("000"));
        }else{
            position.setPositionCode(positionService.selectPositionByParentCode(parentCode));
        }
        positionService.insertPosition(position);
        JSONObject obj = new JSONObject();
        obj.put("msg","新增站点成功");
        return obj;
    }
    /**
     * 编辑部门
     */

    /**
     * @param positionCode
     * @Description: 跳转人员编辑页面
     */
    @RequestMapping(value = "/position/{positionCode}", method = RequestMethod.GET)
    @ResponseBody
    public Position edit(@PathVariable(value="positionCode")String positionCode) throws Exception {
        Position position = new Position();
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(positionCode)){
            position = positionService.selectPositionByPositionCode(positionCode);
        }
        return position;
    }

    @RequestMapping(value = "/position/delStationAreaForm", method = RequestMethod.DELETE)
    @ResponseBody
    public JSONObject delStationArea(String positionCode) throws Exception {
        System.out.println(positionCode+"|||");
        positionService.deletePosition(positionCode);
        JSONObject obj = new JSONObject();
        if(positionCode.length()==6){
            obj.put("msg","删除站区成功");
        }else{
            obj.put("msg","删除站点成功");
        }

        return obj;
    }
}