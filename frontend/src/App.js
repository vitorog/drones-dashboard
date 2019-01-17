import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';

import DronesTable from './feature/dashboard/DronesTable';

class App extends Component {
  render() {
    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <h1 className="App-title">Drones Dashboard</h1>
        </header>
        <div className="App-intro">
          <DronesTable/>
        </div>
      </div>
    );
  }
}

export default App;
