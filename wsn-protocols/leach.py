import math 
import random
import numpy as np
import matplotlib.pyplot as plt
from decimal import *
# Field Dimensions in meters %
xm=100
ym=100
x=0 # added for better display results of the plot
y=0 # added for better display results of the plot
# Number of Nodes in the field %
n=100
# Number of Dead Nodes in the beggining %
dead_nodes=0
# Coordinates of the Sink (location is predetermined in this simulation) %
sinkx=50
sinky=50

# Energy Values %%%
#Initial Energy of a Node (in Joules) %  
Eo=2 #units in Joules
#Energy required to run circuity (both for transmitter and receiver) %
Eelec=50*10**(-9)# units in Joules/bit
ETx=50*10**(-9) # units in Joules/bit
ERx=50*10**(-9) # units in Joules/bit
#Transmit Amplifier Types %
Eamp=100*10**(-12) # units in Joules/bit/m^2 (amount of energy spent by the amplifier to transmit the bits)
# Data Aggregation Energy %
EDA=5*10**(-9)# units in Joules/bit
# Size of data package %
k=4000 # units in bits
# Suggested percentage of cluster head %
p=0.1# a 5 percent of the total amount of nodes used in the network is proposed to give good results
#Number of Clusters %
No=p*n
# Round of Operation %
rnd=0
# Current Number of operating Nodes %
operating_nodes=n
transmissions=0
temp_val=0
flag1stdead=0
print(n)
print(Eelec)
print(Eamp)
print(EDA)
print(ETx)
print(ERx)
##################################################################################################################
class Node:
    id=None # sensor's ID number
    x=  None#rand(1,1)*xm # X-axis coordinates of sensor node
    y= None #rand(1,1)*ym # Y-axis coordinates of sensor node
    e=None      # nodes energy levels (initially set to be equal to "Eo"
    role=0    # node acts as normal if the value is '0', if elected as a cluster head it  gets the value '1' (initially all nodes are normal)
    cluster=0  # the cluster which a node belongs to
    cond=1  # States the current condition of the node. when the node is operational its value is =1 and when dead =0
    rop=0   # number of rounds node was operational
    rleft=0 #rounds left for node to become available for Cluster Head election
    dtch=0 # nodes distance from the cluster head of the cluster in which he belongs
    dts=0  # nodes distance from the sink
    tel=0  # states how many times the node was elected as a Cluster Head
    rn=0   # round node got elected as cluster head
    chid=0 # node ID of the cluster head which the "i" normal node belongs to

    def __init__(self, id, x,y ,e,role=0, cluster=0 , cond=1,rop=0, rleft=0, dtch=0,dts=0  ,tel=0 ,rn=0 ,chid=0  ):
        self.id = id
        self.x = x
        self.y = y
        self.e = e
        self.role= role
        self.cluster= cluster
        self.cond= cond
        self.rop= rop
        self.rleft= rleft
        self.dtch= dtch
        self.dts= dts
        self.tel= tel
        self.rn= rn
        self.chid= chid

class graphPlotting:
    def line(self ,nr,nd,title,xtitle,ytitle):
        plt.plot(nr,nd)
        plt.xlabel(xtitle)
        plt.ylabel(ytitle)
        # giving a title to my graph
        plt.title(title)
        # function to show the plot
        plt.show()

nodes = []
########################################### Creation of the Wireless Sensor Network #######################################
#### Plotting the WSN #### 
for i in range(n):
    ry = round(random.random(),2)*ym
    rx = round(random.random(),2)*xm
    d= math.sqrt((rx-sinkx)**2 + (ry-sinky)**2);
    node= Node(i,rx,ry,Eo,0,0,1,0, 0, 0,d,0 ,0 ,-1 )
    nodes.append(node)
plt.plot([sinkx], [sinky], 'r^',label="BS")

for i in range(n):
    plt.plot([nodes[i].x], [nodes[i].y], 'b+',label="N")
    
plt.xlabel('X/m')
plt.ylabel('Y/m')
plt.show() 


##################################################################################################################
############################################# Set-Up Phase #############################################
nr = []
nd = []
nt = []
ne = []
nch = []
energy=0
while operating_nodes>0 :
        
    # Threshold Value 
    t = (p / (1 - p * (rnd % round(1 / p))))
    # Re-election Value %
    tleft=(rnd % round(1 / p))
    
    # Reseting Previous Amount Of Cluster Heads In the Network %
    CHs= []
        #Reseting Previous Amount Of Energy Consumed In the Network on the Previous Round %
   
       ############################################# Cluster Heads Election #############################################
    for i in range(n):

        nodes[i].cluster=0    # reseting cluster in which the node belongs to
        nodes[i].role=0       # reseting node role
        nodes[i].chid=-1       # reseting cluster head id
        if nodes[i].rleft>0 : 
            nodes[i].rleft=nodes[i].rleft-1

        if (nodes[i].e>0) and (nodes[i].rleft==0) :
            generate=random.uniform(0, 1)
           # print("generate", generate ,"t",t)
            if generate< t :
                #print("generate", generate ,"t",t)
                nodes[i].role=1 # assigns the node role of acluster head
                nodes[i].rn=rnd # Assigns the round that the cluster head was elected to the data table
                nodes[i].tel=nodes[i].tel + 1;   
                nodes[i].rleft=1/p-tleft;    # rounds for which the node will be unable to become a CH
                nodes[i].dts=math.sqrt((sinkx-nodes[i].x)**2 + (sinky-nodes[i].y)**2); # calculates the distance between the sink and the cluster hea
                CHs.append(nodes[i])# sum of cluster heads that have been elected 
                nodes[i].cluster=len(CHs); # cluster of which the node got elected to be cluster head            
    for i in range(n):
        if  nodes[i].role==0 and nodes[i].e>0 and len(CHs)>0: # if node is normal
            mindist = [math.sqrt((nodes[i].x-CHs[0].x)**2 + (nodes[i].y-CHs[0].y)**2),0]
            for j in range(1,len(CHs)):
                dist = [math.sqrt((nodes[i].x-CHs[j].x)**2 + (nodes[i].y-CHs[j].y)**2),j]
                if dist[0] < mindist[0] :
                     mindist = [math.sqrt((nodes[i].x-CHs[j].x)**2 + (nodes[i].y-CHs[j].y)**2),j]
            nodes[i].chid =CHs[mindist[1]].id
            nodes[i].dtch =mindist[0]
        ############################### Energy Dissipation for normal nodes ###############################

    for i in range(n):
        if nodes[i].role == 0 and nodes[i].cond ==1 and len(CHs)>0:
            if nodes[i].e >0 :
                ETx= Eelec*k + Eamp * k * nodes[i].dtch**2
                nodes[i].e=nodes[i].e - ETx
                energy=energy+ETx
                if nodes[nodes[i].chid].e>0 and nodes[nodes[i].chid].cond==1 and nodes[nodes[i].chid].role==1 :
                    ERx=(Eelec+EDA)*k
                    energy=energy+ERx
                    nodes[nodes[i].chid].e=nodes[nodes[i].chid].e - ERx
                    if nodes[nodes[i].chid].e<=0 :  #if cluster heads energy depletes with reception
                        nodes[nodes[i].chid].cond=0
                        dead_nodes=dead_nodes +1
                        operating_nodes= operating_nodes - 1

            if nodes[i].e <=0 :
                dead_nodes=dead_nodes +1;
                operating_nodes= operating_nodes - 1
                nodes[i].cond=0;
                nodes[i].chid=-1;
    




    for i in range(n):
        if nodes[i].role == 1 and nodes[i].cond ==1 :
            if nodes[i].e >0 :
                ETx= (Eelec+EDA)*k + Eamp * k * nodes[i].dts**2;
                nodes[i].e=nodes[i].e - ETx
                energy=energy+ETx
            if nodes[i].e <=0 :
                dead_nodes=dead_nodes +1;
                operating_nodes= operating_nodes - 1
                nodes[i].cond=0

    # Displays Current Round  
    lench = 0 
    for i in range(len(CHs)) : 
        if(CHs[i].cond == 1 ) :
            lench = lench +1
            print('CH ID : ',CHs[i].id)
    print('rnd : ',rnd) 
    print('transmissions : ',transmissions) 
    print('CHs : ', lench) 
    print('dead_nodes : ' ,dead_nodes) 
    print('perating_nodes : ' ,operating_nodes ) 
    print('----------------------------------------------------------------------------------') 
    
    if operating_nodes<n and temp_val==0 :
        temp_val=1
        flag1stdead=rnd
   
    transmissions = transmissions +1
    if(len(CHs)==0): 
        transmissions = transmissions -1
    nr.append([rnd])
    nt.append([transmissions])
    nch.append(len(CHs))
    nd.append([operating_nodes])
    rnd = rnd +1
 
          
    if energy>0 :
        ne.append([transmissions,energy])
##################################################################################################################
print(energy)
graph = graphPlotting()
graph.line(nr,nd,'Operating Nodes per Round','Rounds','Operational Nodes')
graph.line(nt,nd,'Operating Nodes per Transmissions','Transmissions','Operational Nodes')
sum=0;
for i in range(flag1stdead) :
    sum=ne[i][1] + sum

a = []
b = []
for i in range(flag1stdead) : 
    a.append(ne[i][0])
    b.append(ne[i][1])
    
graph.line(a,b,'Energy consumed per Transmission','Transmission','Energy ( J )')

temp1=sum/flag1stdead
temp2=temp1/n
na= []
for i in range(flag1stdead): 
    na.append(temp2)
graph.line(a,na,'Average Energy consumed by a Node per Transmission','Transmission','Energy ( J )')
graph.line(nt,nch,'CHs per Transmissions','Transmission','CHs')
