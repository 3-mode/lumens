/* 
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */

Lumens.SysToolbar_Config = {
    event_type: "SysToolbar",
    buttons:
    [
        {
            name: Lumens.i18n.id_dashboad,
            module_id: "id-dashboard-view"
        },
        {
            name: Lumens.i18n.id_management,
            module_id: "id-management-view"
        },
        {
            name: Lumens.i18n.id_business,
            module_id: "id-desinger-view"
        }
    ]
};

Lumens.DesignNav_Config = {
    event_type: "NavMenu_Design_Tool",
    sections: [
        {name: Lumens.i18n.id_resources},
        {name: Lumens.i18n.id_instruments}
    ]
}