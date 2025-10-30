CREATE VIEW
    user_metadata
AS
    SELECT
        u._user_id
            AS _user_id,
        a.email,
        u.display_name,
        u.first_name,
        u.last_name,
        u._created_at AS _created_at,
        GROUP_CONCAT(r.role_name, ',') AS roles
    FROM
        users u
    LEFT JOIN
        accounts a
        ON
            a._user_id = u._user_id
    LEFT JOIN
        user_roles ur
        ON
            ur._user_id = u._user_id
    LEFT JOIN
        roles r
        ON
            r._role_id = ur._role_id
    GROUP BY
        u._user_id, a.email, u.display_name, u.first_name, u.last_name, u._created_at;

CREATE VIEW
    inventory_metadata
AS
    SELECT 
        item._item_id,
        item_stock._item_stock_id,
        item_stock._created_at,
        item.name AS name,
        item.description AS description,
        item.display_image,
        COALESCE(GROUP_CONCAT(DISTINCT item_category.name), '') AS categories,
        item_brand.name AS brand,
        item_stock.unit_price_php,
        item_stock.minimum_quantity,
        json_group_array(
            json_object(
                '_itemStockStorageLocationId', item_storage_location._item_stock_storage_location_id,
                '_storageLocationId', storage_location._storage_location_id,
                '_createdAt', item_storage_location._created_at, 
                'name', storage_location.name,
                'description', storage_location.description,
                'quantity', item_storage_location.quantity
            )
        ) AS item_storage_locations
    FROM
        items AS item
    INNER JOIN
        item_stocks AS item_stock
        ON
            item._item_id = item_stock._item_id
    INNER JOIN
        item_category_assignments AS item_category_assignment
        ON
            item._item_id = item_category_assignment._item_id
    INNER JOIN
        item_categories AS item_category
        ON
            item_category_assignment._item_category_id = item_category._item_category_id
    INNER JOIN
        item_brands AS item_brand
        ON
            item_stock._item_brand_id = item_brand._item_brand_id
    INNER JOIN
        item_stock_storage_locations AS item_storage_location
        ON
            item_stock._item_stock_id = item_storage_location._item_stock_id
    INNER JOIN
        storage_locations AS storage_location
        ON
            item_storage_location._storage_location_id = storage_location._storage_location_id
    GROUP BY
        item._item_id,
        item_stock._item_stock_id,
        item._created_at,
        item.name,
        item_category.name,
        item_brand.name
    ORDER BY
        item._item_id;

CREATE VIEW item_stock_locations_view
AS
    SELECT
        item_stock_storage_locations._item_stock_storage_location_id,
        item_stock_storage_locations._item_stock_id,
        item_stock_storage_locations._storage_location_id,
        storage_locations.name AS name,
        storage_locations.description AS description,
        item_stock_storage_locations.quantity AS quantity,
        item_stock_storage_locations._created_at AS _created_at
    FROM
        item_stock_storage_locations
    INNER JOIN
        storage_locations 
        ON
        item_stock_storage_locations._storage_location_id = storage_locations._storage_location_id
    ORDER BY 
        tem_stock_storage_locations._item_stock_storage_location_id;
