package cn.zxJava.groupentity;

import cn.zxJava.domain.TbSpecification;
import cn.zxJava.domain.TbSpecificationOption;

import java.io.Serializable;
import java.util.List;

/*
* 模块的组合类
* */
public class Specification implements Serializable {
    //规格对象
    private TbSpecification tbSpecification;
    //规格项集合
    private List<TbSpecificationOption> tbSpecificationOptionList;

    public TbSpecification getTbSpecification() {
        return tbSpecification;
    }

    public void setTbSpecification(TbSpecification tbSpecification) {
        this.tbSpecification = tbSpecification;
    }

    public List<TbSpecificationOption> getTbSpecificationOptionList() {
        return tbSpecificationOptionList;
    }

    public void setTbSpecificationOptionList(List<TbSpecificationOption> tbSpecificationOptionList) {
        this.tbSpecificationOptionList = tbSpecificationOptionList;
    }
}
