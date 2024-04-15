import React from "react";
import { Route, Routes } from "react-router-dom";
import Login from "../Public/Login.jsx";
import Home from "../Public/Home.jsx";
import Register from "../Public/Register.jsx";
import BecomeASeller from "../Private/Seller/BecomeASeller.jsx";
import Account from "../Private/Common/Account.jsx";
import Cart from "../Private/Customer/Cart.jsx";
import Order from "../Private/Customer/Order.jsx";
import App from "../App.jsx";
import VerifyOTP from "../Public/VerifyOtp.jsx";

const AllRoutes = () => {
  const user = {
    userId: "123",
    username: "harsh",
    role: "CUSTOMER",
    authenticated: false ,
    accessExpiration: 3600,
    refreshExpirarion: 129600,
  };

  const { role, authenticated } = user;
  const routes = [];

  if (authenticated) {
    if (role === "CUSTOMER") {
      routes.push(
        <Route key="home" path="/home" element={<Home />} />,
        <Route key="account" path="/account" element={<Account />} />,
        <Route key="verifyOTP" path="/verifyOTP" element={<VerifyOTP />} />,
        <Route key="cart" path="/cart" element={<Cart />} />,
        <Route key="orders" path="/orders" element={<Order />} />
      );
    } else if (role === "SELLER") {
      routes.push(
        <Route key="becomeSeller" path="/seller" element={<BecomeASeller />} />
      );
    }
  } else {
    routes.push(
      <Route key="login" path="/login" element={<Login />} />,
      <Route key="register" path="/register" element={<Register />} />
    );
  }

  return (
    <Routes>
      <Route path="/" element={<App />}>
        {routes}
      </Route>
    </Routes>
  );
};

export default AllRoutes;
