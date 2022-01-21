## MicroZigbee2MQTT

MicroZigbee2MQTT is very light (YAML based) configuration setup for [Zigbee2MQTT](https://www.zigbee2mqtt.io/)
and allows you for creating simple scenes using [supported](https://www.zigbee2mqtt.io/supported-devices/) Zigbee
devices.

### Prerequisites

MicroZigbee2MQTT requires following components:

* MQTT broker (for example [Mosquitto](https://mosquitto.org/download/))
* [Zigbee2MQTT](https://www.zigbee2mqtt.io/guide/installation/)

### Run

You can run MicroZigbee2MQTT as a Docker container and native Java application as well.

#### Docker

You cun run MicroZigbee2MQTT using Docker:

    docker run -d -p 8080:8080 -v application.yml:/opt/application.yml --name microzigbee2mqtt coderion/microzigbee2mqtt:latest

#### JAR (Java application)

Alternatively you can run application as a standard Java JAR:

    MICRONAUT_CONFIG_FILES=application.yml java -jar zigbee2mqtt-*.jar    

### Configuration

The whole configuration is included in file *application.yml* and should be provided during startup application (see
above).

Below example of [simple config](src/main/resources/application.yml) with one scene "When you press the button the lamp
turns on or off":

    micronaut:
      application:
        name: microzigbee2mqtt
    
    logger:
      levels:
        pl:
          micronaut: INFO
    
    mqtt: (1)
      client:
       client-id: ${random.uuid}
       server-uri: tcp://localhost:1883
       user-name:
       password:

    email: (2)
        enabled: false
        recipient: recipient@gmail.com
        smtp:
            host: smtp.gmail.com
            port: 465
            ssl: true
            username: your-email@gmail.com
            password: your-password
    
    pushover: (3)
        enabled: false
        user: your-username
        token: your-application-token

    zigbee2mqtt: (4)
      queue-prefix: zigbee2mqtt/d/
      devices:
        - name: salon/button
        - name: salon/lamp
        - name: salon/button4gang
      scenes:
        - name: button_enables_lamp
          device: salon/button
          property: action
          value: single
          triggers:
            - device: salon/lamp
              property: state
              value: toggle
        - name: button_enables_another_button
          device: salon/button
          property: action
          value: double
          triggers:
            - device: salon/button4gang
              property: action
              value: 1_single
            - do-not-disable: button_enables_lamp
        - name: blink_lamp
          cron: "0/15 * * * * ?"
          triggers:
            - device: salon/lamp
              property: state
              value: toggle
        - name: button_disables_scene
          device: salon/button4gang
          property: action
          value: 1_single
          triggers:
            - disable: button_enables_lamp
        - name: button_enables_scene
          device: salon/button4gang
          property: action
          value: 2_single
          triggers:
            - enable: button_enables_lamp

Section _mqtt_ (1) defines address of your MQTT Broker (and optionally username and password if needed)

Section _email_ (2) defines SMTP server settings to send e-mail notifications

Section _pushover_ (3) defines [Pushover](https://pushover.net/) settings to send push notifications

Section _zigbee2mqtt_ (4) defines your scenes:

* _queue-prefix_ - prefix added to device name to indicate MQTT topic name (it's highly recommended do not use _"
  zigbee2mqtt/"_ which is default topic in Zigbee2MQTT and contains many internal system messages can cause errors in
  logs)
* _devices_ - list of your zigbee devices (names are frindly name from
  Zigbee2MQTT [configuration file](https://www.zigbee2mqtt.io/guide/configuration/devices-groups.html))
  . Name can contain slashes ("/") for grouping devices.
* _scenes_ - list of your scenes. Each scene includes:
    * _name_ - any human readable name describing your scene
    * _cron_ - [CRON](https://en.wikipedia.org/wiki/Cron) expression to trigger action periodically
    * _device_ - name of device (its friendly name) which start the scene
    * _property_ - name of property in MQTT message payload which should start the scene
    * _value_ - value in MQTT message payload which should start the scene
    * _notification_ - notification settings
        * _email_ - is e-mail notification enabled
        * _pushover_ - is push notification (via [Pushover](https://pushover.net/)) enabled
    * _triggers_ - list of triggers (MQTT messages to publish) should be done if the scenes occures:
        * _device_ - name of device triggered in scene
        * _property_ - name of property in MQTT message payload which should be published during the scene
        * _value_ - value in MQTT message payload which should be published during the scene
        * _do-not-disable_ - name of scene that shouldn't be disabled in the nested triggers
        * _disable_ - name of scene to be disabled
        * _enable_ - name of scene to be enabled

In example above when the button is pressed (and hence in topic _zigbee2mqtt/button_ is published message with
payload `{"action": "single"}`) another message will be published to topic
_zigbee2mqtt/lamp_ (with payload `{"state": "toggle"}`) which turns the lamp on/off.

### API

* _/state_ (for example http://localhost:8080/state for your local application) - state of all devices updated each time
  MQTT message is published
* _/state/battery_ - state of battery for each device providing property _battery_ in MQTT message
* _/state/voltage_ - state of voltage for each device providing property _voltage_ in MQTT message

### To do

Roadmap for the nearest future include:

* store events in optional MongoDB database for future statistics
* define scene using sunrise & sunset time
