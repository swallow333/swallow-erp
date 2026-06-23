-- 1. 插入角色
INSERT INTO sys_role (role_code, role_name, description) VALUES
                                                             ('SUPER_ADMIN', '超级管理员', '拥有所有权限'),
                                                             ('ADMIN', '管理员', '管理日常业务'),
                                                             ('USER', '普通用户', '只读权限');

-- 2. 插入菜单（示例）
INSERT INTO sys_menu (menu_name, parent_id, menu_type, path, component, permission, icon, sort_order) VALUES
                                                                                                          ('首页', 0, 1, '/dashboard', 'dashboard', 'dashboard:view', 'HomeOutlined', 1),
                                                                                                          ('系统管理', 0, 1, '/system', 'system', 'system:view', 'SettingOutlined', 100),
                                                                                                          ('用户管理', 2, 2, '/system/user', 'user', 'user:list', 'UserOutlined', 101),
                                                                                                          ('角色管理', 2, 2, '/system/role', 'role', 'role:list', 'TeamOutlined', 102),
                                                                                                          ('商品管理', 0, 1, '/product', 'product', 'product:view', 'ShoppingOutlined', 10),
                                                                                                          ('商品列表', 5, 2, '/product/list', 'productList', 'product:list', NULL, 11),
                                                                                                          ('分类管理', 5, 2, '/product/category', 'productCategory', 'category:list', NULL, 12),
                                                                                                          ('采购管理', 0, 1, '/purchase', 'purchase', 'purchase:view', 'ImportOutlined', 20),
                                                                                                          ('采购订单', 8, 2, '/purchase/order', 'purchaseOrder', 'purchase:order:list', NULL, 21),
                                                                                                          ('采购入库', 8, 2, '/purchase/stock-in', 'stockIn', 'purchase:stock:in', NULL, 22),
                                                                                                          ('销售管理', 0, 1, '/sale', 'sale', 'sale:view', 'ExportOutlined', 30),
                                                                                                          ('销售订单', 11, 2, '/sale/order', 'saleOrder', 'sale:order:list', NULL, 31),
                                                                                                          ('库存管理', 0, 1, '/inventory', 'inventory', 'inventory:view', 'DatabaseOutlined', 40),
                                                                                                          ('实时库存', 13, 2, '/inventory/stock', 'stock', 'inventory:stock', NULL, 41),
                                                                                                          ('库存流水', 13, 2, '/inventory/flow', 'stockFlow', 'inventory:flow', NULL, 42);

-- 3. 给超级管理员分配所有权限（关联所有菜单）
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, id FROM sys_menu;

-- 4. 给管理员分配部分权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 2, id FROM sys_menu WHERE id IN (1, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);

-- 5. 给普通用户分配只读权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 3, id FROM sys_menu WHERE menu_type = 2;

-- 6. 分配用户角色（假设已有用户）
INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1);