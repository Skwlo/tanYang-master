import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import { getWeekMonthQuarterYear } from '/@/utils';

//列表数据
export const columns: BasicColumn[] = [
  {
    title: '销售记录 ID',
    align:"center",
    dataIndex: 'saleRecordId'
  },
  {
    title: '畜只 ID',
    align:"center",
    dataIndex: 'livestockId'
  },
  {
    title: '单价',
    align:"center",
    dataIndex: 'unitPrice'
  },
  {
    title: '数量',
    align:"center",
    dataIndex: 'quantity'
  },
  {
    title: '小计金额',
    align:"center",
    dataIndex: 'amount'
  },
];

//查询数据
export const searchFormSchema: FormSchema[] = [
  {
    label: "销售记录 ID",
    field: 'saleRecordId',
    component: 'Input',
  },
  {
    label: "畜只 ID",
    field: 'livestockId',
    component: 'Input',
  },
  {
    label: "单价",
    field: 'unitPrice',
    component: 'InputNumber',
  },
  {
    label: "数量",
    field: 'quantity',
    component: 'InputNumber',
  },
  {
    label: "小计金额",
    field: 'amount',
    component: 'InputNumber',
  },
];

//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '销售记录 ID',
    field: 'saleRecordId',
    component: 'Input',
    dynamicRules: ({model,schema}) => [
      { required: true, message: '请输入销售记录 ID!'},
    ],
  },
  {
    label: '畜只 ID',
    field: 'livestockId',
    component: 'Input',
    dynamicRules: ({model,schema}) => [
      { required: true, message: '请输入畜只 ID!'},
    ],
  },
  {
    label: '单价',
    field: 'unitPrice',
    component: 'InputNumber',
    dynamicRules: ({model,schema}) => [
      { required: true, message: '请输入单价!'},
    ],
  },
  {
    label: '数量',
    field: 'quantity',
    component: 'InputNumber',
    dynamicRules: ({model,schema}) => [
      { required: true, message: '请输入数量!'},
    ],
  },
  {
    label: '单位（头）',
    field: 'unit',
    component: 'Input',
    defaultValue: '头', // 默认单位为"头"
    show: false // 隐藏单位输入框
  },
  {
    label: '小计金额',
    field: 'amount',
    component: 'InputNumber',
    dynamicRules: () => [] // 移除必填规则
  },
  // 主键隐藏字段
  {
    label: '',
    field: 'id',
    component: 'Input',
    show: false
  },
];

// 高级查询数据
export const superQuerySchema = {
  saleRecordId: {title: '销售记录 ID',order: 0,view: 'text', type: 'string',},
  livestockId: {title: '畜只 ID',order: 1,view: 'text', type: 'string',},
  unitPrice: {title: '单价',order: 2,view: 'number', type: 'number',},
  quantity: {title: '数量',order: 3,view: 'number', type: 'number',},
  amount: {title: '小计金额',order: 5,view: 'number', type: 'number',},
};

export function getBpmFormSchema(_formData): FormSchema[]{
  return formSchema;
}
