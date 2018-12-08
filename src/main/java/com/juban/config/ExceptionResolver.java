/*
package com.juban.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.juban.common.ConstantCode;
import com.juban.common.MyException;
import org.apache.catalina.connector.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

@Component
public class ExceptionResolver implements HandlerExceptionResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        JSONObject json = new JSONObject();
        //MyException处理
        if (ex instanceof MyException) {
            MyException MyException = (MyException) ex;
            json.put(ConstantCode.RES_CODE, MyException.getErrorCode());
            json.put(ConstantCode.RES_MSG, MyException.getMessage());
        } */
/*else if (ex instanceof ValidateException) {
            ValidateException validateException = (ValidateException) ex;
            json.put(ConstantCode.RET_CODE, validateException.getErrorCode());
            json.put(ConstantCode.RET_MSG, validateException.getMessage());
        } else if (ex.getClass().isAssignableFrom(DataAccessException.class)) {
            json.put(ConstantCode.RET_CODE, ConstantCode.DATABASE_ERROR);
            json.put(ConstantCode.RET_MSG, "数据库请求异常！");
        } else if (ex.getClass().isAssignableFrom(ApiRequestException.class)) {
            json.put(ConstantCode.RET_CODE, ConstantCode.DATA_PARSE_ERROR);
            json.put(ConstantCode.RET_MSG, "接口请求错误！");
        } else if (ex.getClass().isAssignableFrom(RequestParamValidException.class)) {
            RequestParamValidException requestParamValidException = (RequestParamValidException) ex;
            json.put(ConstantCode.RET_CODE, ConstantCode.DATA_PARSE_ERROR);
            json.put(ConstantCode.RET_MSG, requestParamValidException.getMessage());
        }*//*
 else {
            json.put(ConstantCode.UNKOWN_ERROR, ConstantCode.UNKOWN_ERROR);
            json.put(ConstantCode.RES_MSG, "未知错误!");
        }

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(Response.SC_OK);

        OutputStream os = null;
        InputStream is = null;
        try {
            is = new ByteArrayInputStream(JSON.toJSONString(json).getBytes("UTF-8"));
            os = response.getOutputStream();
            IOUtils.copy(is, os);
        } catch (Exception e) {
            LOGGER.error("Failed to serialize the object to json for exception handling.", e);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }
        return null;
    }
}

*/
