import { createContext, useContext, useEffect, useState } from "react";
import {
  getCartItems,
  removeCartItem as removeCartItemApi,
  addToCart as addToCartApi,
} from "../services/cartService";

const CartContext = createContext();

export function CartProvider({ children }) {
  const [cartItemCount, setCartItemCount] = useState(0);

  const fetchCartCount = async () => {
    try {
      if (!localStorage.getItem("token")) {
        setCartItemCount(0);
        return;
      }
      const cart = await getCartItems();
      setCartItemCount(cart.itemCount);
    } catch {
      setCartItemCount(0);
    }
  };

  useEffect(() => {
    fetchCartCount();
  }, []);

  const addToCart = async (bookId) => {
    await addToCartApi(bookId);
    setCartItemCount(prev => prev + 1);
  };

  const removeCartItem = async (bookId) => {
    await removeCartItemApi(bookId);
    await fetchCartCount();
  };

  return (
    <CartContext.Provider
      value={{
        cartItemCount,
        fetchCartCount,
        addToCart,
        removeCartItem,
      }}
    >
      {children}
    </CartContext.Provider>
  );
}

export const useCart = () => useContext(CartContext);