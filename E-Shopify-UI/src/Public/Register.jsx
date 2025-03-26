import React, { useState } from 'react';

const Register = () => {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [nameError, setNameError] = useState('');
  const [emailError, setEmailError] = useState('');
  const [passwordError, setPasswordError] = useState('');

  const handleNameChange = (e) => {
    const newName = e.target.value.replace(/(^|\s)\S/g, (l) => l.toUpperCase());
    setName(newName);
    if (newName && !/^[a-zA-Z\s]+$/.test(newName)) {
      setNameError('Name should contain only alphabetical characters');
    } else {
      setNameError('');
    }
  };

  const handleEmailChange = (e) => {
    const newEmail = e.target.value;
    setEmail(newEmail);
    if (newEmail && !/^\S+@\S+\.\S+$/.test(newEmail)) {
      setEmailError('Please enter a valid email address');
    } else {
      setEmailError('');
    }
  };

  const handlePasswordChange = (e) => {
    const newPassword = e.target.value;
    setPassword(newPassword);
    if (newPassword && !/(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}/.test(newPassword)) {
      setPasswordError('Password must contain at least one uppercase letter, one lowercase letter, one number, and be at least 8 characters long');
    } else {
      setPasswordError('');
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!nameError && !emailError && !passwordError) {
      console.log('Registration data:', { name, email, password });
      setName('');
      setEmail('');
      setPassword('');
    }
  };

  return (
    <div className="flex justify-center items-center h-screen bg-gray-100">
      <div className="bg-white p-8 rounded-lg shadow-md">
        <h2 className="text-2xl font-semibold mb-6">Create an account</h2>
        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <input 
              type="text" 
              placeholder="Name" 
              value={name} 
              onChange={handleNameChange} 
              className="w-full px-3 py-2 rounded border border-gray-300 focus:outline-none focus:border-blue-500"
            />
            {nameError && <p className="text-red-500 text-sm">{nameError}</p>}
          </div>
          <div className="mb-4">
            <input 
              type="email" 
              placeholder="Email" 
              value={email} 
              onChange={handleEmailChange} 
              className="w-full px-3 py-2 rounded border border-gray-300 focus:outline-none focus:border-blue-500"
            />
            {emailError && <p className="text-red-500 text-sm">{emailError}</p>}
          </div>
          <div className="mb-6">
            <input 
              type="password" 
              placeholder="Password" 
              value={password} 
              onChange={handlePasswordChange} 
              className="w-full px-3 py-2 rounded border border-gray-300 focus:outline-none focus:border-blue-500"
            />
            {passwordError && <p className="text-red-500 text-sm">{passwordError}</p>}
          </div>
          <button type="submit" className="w-full bg-yellow-500 text-white font-semibold py-2 rounded hover:bg-yellow-600 focus:outline-none focus:bg-yellow-600">Create your account</button>
        </form>
        <p className="mt-4 text-sm text-center text-gray-600">By creating an account, you agree to Flipkart's <a href="#" className="text-blue-500 hover:underline">Terms of Use</a> and <a href="#" className="text-blue-500 hover:underline">Privacy Policy</a>.</p>
      </div>
    </div>
  );
};

export default Register;
