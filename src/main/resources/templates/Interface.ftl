package ${package_name}.service;
import com.evada.inno.core.service.IBaseService;
import ${package_name}.model.${table_name};
import ${package_name}.dto.${table_name}DTO;
/**
* ������${table_annotation} ����ʵ�ֲ�ӿ�
* @author ${author}
* @date ${date}
*/
public interface I${table_name}Service extends IBaseService<${table_name},String> {

    /**
    * ����������Id��ȡDTO
    * @param id
    */
    ${table_name}DTO findDTOById(String id)throws Exception;

    ${table_name}DTO create${table_name}(${table_name}DTO ${table_name?uncap_first}DTO) throws Exception;

    void delete${table_name}(String id) throws Exception;

    ${table_name}DTO update${table_name}(${table_name}DTO ${table_name?uncap_first}DTO) throws Exception;

}

