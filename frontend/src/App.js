import React, { useEffect, useState } from 'react';
import axios from 'axios';
import InventoryItem from './components/InventoryItem';
import './App.css';

function App() {
    const [items, setItems] = useState([]);
    const [history, setHistory] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        axios.get('http://localhost:8080/yks_item')
            .then(res => {
                if (Array.isArray(res.data)) {
                    setItems(res.data);
                } else {
                    console.error('Invalid response: items is not array', res.data);
                    setItems([]);
                }
                setLoading(false);
            })
            .catch(err => {
                console.error("Error fetching items:", err);
                setLoading(false);
            });
    }, []);

    useEffect(() => {
        axios.get('http://localhost:8080/purchase/history')
            .then(res => {
                if (Array.isArray(res.data)) {
                    setHistory(res.data);
                } else {
                    console.error('Invalid response: history is not array', res.data);
                    setHistory([]);
                }
            })
            .catch(err => console.error("Error fetching history:", err));
    }, []);

    const handleBuy = (item_id) => {
        axios.post(`http://localhost:8080/purchase?item_id=${item_id}`)
            .then(res => {
                alert(res.data);
                window.location.reload();
            })
            .catch(err => {
                console.error("Purchase failed:", err);
                alert("Purchase failed");
            });
    };

    if (loading) return (
        <div className="loading-container">
            <div className="loading-spinner">Loading...</div>
        </div>
    );

    return (
        <div className="app-container">
            <div className="section-header">
                <h1 className="app-header">🛒 Inventory</h1>
            </div>
            <ul className="inventory-grid">
                {items.map(item => (
                    <InventoryItem key={item.item_key} item={item} onBuy={handleBuy} />
                ))}
            </ul>

            <div className="history-container">
                <h2 className="section-header">🧾 Purchase History</h2>
                <ul className="history-list">
                    {history.map(order => (
                        <li key={order.order_id} className="history-item">
                            {order.item_id} - {order.item_description}
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );
}

export default App;
