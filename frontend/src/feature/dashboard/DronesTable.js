import React, { Component } from "react";
import { Table } from "reactstrap";
import SockJS from "sockjs-client";
import Stomp from "stomp-websocket";

const DRONES_SERVER = "http://localhost:9001/drones-websocket";
const DRONES_TOPIC = "/topic/drones";

export default class DronesTable extends Component {
  constructor(props) {
    super(props);

    this.state = {
      stompClient: null,
      drones: []
    };

    this.renderTable = this.renderTable.bind(this);
    this.connect = this.connect.bind(this);
    this.disconnect = this.disconnect.bind(this);
  }

  connect() {
    const socket = new SockJS(DRONES_SERVER);
    const stompClient = Stomp.over(socket);
    stompClient.connect({}, () => {
      stompClient.subscribe(DRONES_TOPIC, message => {
        this.setState({ drones: JSON.parse(message.body) });
      });
    });
    this.setState({ stompClient: stompClient });
  };

  disconnect() {
    const stompClient = this.state.stompClient;
    if (stompClient != null) {
      stompClient.disconnect();
    }
  }

  componentDidMount() {
    this.connect();
  }

  componentWillUnmount() {
    this.disconnect();
  }

  renderTable() {
    return (<Table dark>
      <thead>
      <tr>
        <th>Drone Id</th>
        <th>Latitude</th>
        <th>Longitude</th>
        <th>Speed</th>
        <th>Status</th>
      </tr>
      </thead>
      <tbody>
      {this.state.drones.map(
        d => (<tr key={d.droneId} className={d.hasStopped ? "tr-stopped" : ""}>
          <th scope={"row"}>{d.droneId}</th>
          <td>{d.latitude}</td>
          <td>{d.longitude}</td>
          <td>{Number(d.speed).toFixed(4)} m/s</td>
          <td>{d.hasStopped ? "STOPPED" : "OK"}</td>
        </tr>))}

      </tbody>
    </Table>);
  }

  render() {
    return (<div>
      {this.state.drones.length > 0 ? this.renderTable() : <p>No data to
        display.</p>}
    </div>);
  }
}