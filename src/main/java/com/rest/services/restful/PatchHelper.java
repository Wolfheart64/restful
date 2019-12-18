package com.rest.services.restful;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.services.restful.entities.EmployeeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.json.JsonPatch;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class PatchHelper {

    private final ObjectMapper mapper;

    private final Validator validator;


    public <T> T patch(JsonPatch patch, EmployeeEntity targetBean, Class<T> beanClass) throws Exception {
        JsonStructure target = mapper.convertValue(targetBean, JsonStructure.class);
        JsonValue patched = applyPatch(patch, target);
        return convertAndValidate(patched, beanClass);
    }

    private JsonValue applyPatch(JsonPatch patch, JsonStructure target) throws Exception {
        try {
            return patch.apply(target);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    private <T> T convertAndValidate(JsonValue jsonValue, Class<T> beanClass) {
        T bean = mapper.convertValue(jsonValue, beanClass);
        validate(bean);
        return bean;
    }

    private <T> void validate(T bean) {
        Set<ConstraintViolation<T>> violations = validator.validate(bean);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }


}
