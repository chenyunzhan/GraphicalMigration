package com.cloud.fiber;

/**
 * ���ڹ���˺е�λ�ã���С�Ƕ�̬��������ġ�����û��Ҫ����Ǩ�ơ�
 * ��ϵͳ�е����֣�����ɽˮ�ܱ๫˾001GF
 * @author Administrator
 *
 */
public class BatchFiber {
	/**
	 * ��ѯ��ϵͳ��sql
	 * SELECT * FROM t_rm_physical_device AS a WHERE a.PHYSICAL_DEVICE_NAME = '������GF007';
		SELECT b.POS_X, b.POS_Y, b.TP_HEIGHT, b.TP_LENGTH, b.TP_WIDTH FROM t_rm_module AS b WHERE b.UPPER_CLASS_ID = 296 AND b.UPPER_RESOURCE_ID = 1385349371359 AND b.UPPER_RESOURCE_SHARDING_ID = 123702;
		SELECT * FROM t_rm_module AS b WHERE b.UPPER_CLASS_ID = 296 AND b.UPPER_RESOURCE_ID = 1385349371359 AND b.UPPER_RESOURCE_SHARDING_ID = 123702;
		SELECT * FROM t_rm_optical_terminal AS c WHERE c.MODULE_ID = 1385628767576;
	 */
	
	/**
	 * ��ѯ��ϵͳ��sql
	 * select * from f_mi_node_panel
		select * from f_mi_store;
		select * from f_fiber_node where fiber_node_no like '%����ɽˮ�ܱ๫˾001GF';
		select * from f_mi_store a inner join f_mi_node_panel c on a.map_id=c.map_id
                           			inner join f_fiber_node b on a.mis_id=b.fiber_node_id where b.fiber_node_no = '����ɽˮ�ܱ๫˾001GF'
		select * from f_mi_node_panel a where a.map_id = 995903
	 */
}
