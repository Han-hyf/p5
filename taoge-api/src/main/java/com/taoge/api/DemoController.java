//package com.taoge.api;
//
//import com.alibaba.fastjson.JSONObject;
//import com.taoge.framework.controller.BaseApiController;
//import com.taoge.param.InfoParam;
//import com.taoge.persistent.entity.Order;
//import com.taoge.persistent.service.DemoService;
//import com.taoge.persistent.service.GetUserInfoParam;
//import com.taoge.persistent.service.OrderService;
//import com.taoge.vo.BaseParam;
//import com.taoge.vo.UserInfo;
//import lombok.extern.slf4j.Slf4j;
//import org.bouncycastle.asn1.ocsp.ResponseData;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//
//@RestController
//@RequestMapping("/test")
//@Slf4j
//public class DemoController extends BaseApiController {
//
//    @Resource
//    DemoService demoService;
//    @Resource
//    HttpServletRequest request;
//    @Resource
//    OrderService orderService;
//
//    @PostMapping("/test")
//    @ResponseBody
//    public ResponseData test() {
//        return new ResponseData();
//    }
//
//    @PostMapping("/info")
//    @ResponseBody
//    public ResponseData<UserInfo> info(@RequestBody InfoParam param) {
////        if (null == param.getId() || !"1".equals(param.getId())) {
////            throw new ParamException("id is null");
////        }
//        // 另一种参数处理方式，mapstruct
//        // 帮你生成 get set 代码
////        HttpServletRequest request;
//
//        GetUserInfoParam getUserInfoParam = (GetUserInfoParam) param.convertTo(GetUserInfoParam.class);
//        UserInfo userInfo = demoService.getInfo(getUserInfoParam);
//
//
//        ParamValidateUtil.validateParam(param);
//        // 很多人钻牛角尖
//        // 反射一定性能差，效率低，在代码中可以优化，用个map就能解决
//        // 工具类中增加map，key就是Class类型
//        return new ResponseData();
//    }
//
//    @PostMapping("/getToken")
//    @ResponseBody
//    @NotLogin
//    public ResponseData<UserToken> getToken() {
//        UserToken userToken = TokenUtil.generateToken(null);
//        return ResponseData.success(userToken);
//    }
//
//    @PostMapping("/login")
//    @ResponseBody
//    @NotLogin
//    public ResponseData<UserToken> login() {
//        // 登录业务操作之后，得到的用户id
//        Long userId = 1000L;
//        UserToken userToken = TokenUtil.generateToken(userId);
//        return ResponseData.success(userToken);
//    }
//
//    @PostMapping("/getUserInfo")
//    @ResponseBody
//    public ResponseData<UserToken> getUserInfo() {
//
//        UserToken userToken = getUserToken();
////        HttpServletRequest request = getRequest();
////        getRequest().getHeader("xxx");
//
//        if (userToken.isGuest()) {
//            //  游客限制的一些功能
//        }
//
//        return ResponseData.success(userToken);
//    }
//
//    @PostMapping("/list")
//    @ResponseBody
//    public ResponseData<UserToken> list(@RequestBody InfoParam param) {
//        log.info("list param:{}", JSONObject.toJSONString(param));
//
//        return ResponseData.success();
//    }
//
//    @Override
//    @RequestMapping("/zzzz")
//    public ResponseData execute(BaseParam param) {
//
//        // 获取用户信息 -> 通用方法
//
//        // 获取request -> getRequest()
//
//        // getIp()
//
//
//        return null;
//    }
//
//    @PostMapping("/getOrder")
//    @ResponseBody
//    public ResponseData<?> getOrder() {
//        Order order = orderService.selectByPrimaryKey(1L);
//        return ResponseData.success(order);
//    }
//
//    @PostMapping("/insertOrder")
//    @ResponseBody
//    public ResponseData<?> insertOrder() {
//        Order order = RandomDataGenerator.generateOrder();
//        orderService.insert(order);
//
//        orderService.getmap
//
//        return ResponseData.success();
//    }
//
//
//}
