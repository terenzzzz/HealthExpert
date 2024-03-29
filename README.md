# HealthExpert

毕设应用（安卓）

# Project Brief

- **设计和开发一个协作的移动传感系统，连接移动或可穿戴设备，以支持普遍的健康应用**
  - 对可用于个人健康数据收集的移动和可穿戴设备的最新综述
  - 在普遍的医疗保健案例的特定背景下，与最终用户共同确定的关键需求和挑战
  - 支持高效数据收集、处理、存储、共享和利用的协作移动传感平台的原型设计
  - 采用可行技术的平台实施
  - 允许使用来自该平台的数据进行生活质量评估的数据挖掘方法
- 该项目团队将开发一个协作移动传感系统来收集、存储、共享和利用个人健康数据。
- 从移动或可穿戴设备收集的数据包括与日常步数、位置、活动、心率、血压、情绪等相关的个人信息
- 专注于设计移动传感应用程序以有效收集个人健康数据
- 学生需要专注于移动编程和云技术来实施该系统

# API Doc

- https://www.terenzzzz.com:9999/apidoc/

# Design

- https://www.figma.com/file/2rSFX3lfBXgDCZYWQHIaEK/Health-Expert?node-id=4%3A659&t=xlKEFISCkBVcsJcn-0

# TODO

- Main Feature

  - [x] Steps Tracker
    - [x] Can get user's steps
    - [x] Can get steps in the baground(Service)
    - [x] Can update to database(every 30s)
    - [x] Can get data from database
    - [x] Can show the data in the activity
    - [x] Can plot the graph
    - [x] Can set goal

  - [x] Training Tracker
    - [x] Can select mode (walking/running/cycling)
    - [x] Can add title
    - [x] Can get metric about the training during recording
    - [x] Can get location during training(Service)
    - [x] Can show location in the map
    - [x] Can add training to database
    - [x] Can add get trainining record from database
    - [x] Can delete record
    - [x] Can set goal

  - [x] Water Drinking Reminder
    - [x] Can get today's Drinking Records
    - [x] Can get specific Record
    - [x] Can calculate the metrics
    - [x] Can add Drinking record
    - [x] Can edit Drinking record
    - [x] Can delete Drinking record
    - [x] Can set goal
    - [x] Can push notification

  - [x] Calories Records
    - [x] Can get today's Calories Records
    - [x] Can get specific Record
    - [x] Can calculate the metrics
    - [x] Can add calories record
    - [x] Can edit calories record
    - [x] Can delete calories record
    - [x] Can set goal

  - [x] Medication Reminder
    - [x] Can get today's medication reminder
    - [x] Can get pending medication reminder
    - [x] Can add medication reminder to the database
    - [x] Can change the status of reminder to done
    - [x] Can push notification
    - [x] Can delete reminder
    - [x] Can edit pending reminder
    
  - [x] Sleep Tracker
    - [x] Can see the latest Record of sleep
    - [x] Can tracking Humidity properly
    - [x] Can tracking Temperature properly
    - [x] Can tracking Pressure properly
    - [x] Can tracking Light properly
    - [x] Can add sleep to database
    - [x] Can view the explaination of each matric
    
  - [x] Heart Rate Record
    - [x] Can use camera
    - [x] Can get preview of the camera
    - [x] Can get grey scal of the image
    - [x] Can calculate the color different
    - [x] Can work out Heart rate
    - [x] Can record heart rate
    - [x] Can show heart rate record
    
  - [ ] Menstrual period
   

- Aside
  - [x] Can get Articles
  - [x] User Profile
  - [x] Can view History
    - [x] Can select date
    - [x] Can show the data of the specific date
    - [x] Can see All block of data
  - [x] Body index monitoring
  - [ ] Emergency contact
  - [ ] Store data in local

