import React from 'react';

function InventoryItem({ item, onBuy }) {
    const { item_id, item_description, img_url, stock } = item;
    const isAvailable = stock > 0;

    return (
        <li className="inventory-item">
            <div className="item-image-container">
                <img src={img_url} alt={item_id} className="item-image" />
            </div>
            <div className="item-content">
                <h3 className="item-title">{item_id}</h3>
                <p className="item-description">{item_description}</p>

                {isAvailable ? (
                    <p className="item-stock-info" style={{ color: 'green', fontWeight: 'bold' }}>
                        Available ({stock} in stock)
                    </p>
                ) : (
                    <p className="item-stock-info" style={{ color: 'red', fontWeight: 'bold' }}>
                        Out of Stock
                    </p>
                )}

                <button
                    onClick={() => onBuy(item_id)}
                    disabled={!isAvailable}
                    className={isAvailable ? "buy-button buy-button-available" : "buy-button buy-button-disabled"}
                >
                    Buy
                </button>
            </div>
        </li>
    );
}

export default InventoryItem;
