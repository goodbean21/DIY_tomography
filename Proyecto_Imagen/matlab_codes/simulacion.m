function [IR, IRf, IR2, t] = simulacion()
%%
close all
clear all
clc

tic

sliceZDim = 1;
PerimetroReal = 80;

P = phantom('Modified Shepp-Logan', 256);
theta = 0:180;

R = radon(P, theta);

IR = iradon(R, theta,'nearest');

BW = im2bw(IR,0.7);
BW = imfill(BW, 'holes');
BW = imopen(BW,strel('disk',1));

props = regionprops('table', BW, 'Perimeter');

pDim = PerimetroReal/props.Perimeter;

IRf = iradon(R, theta, 'Ram-Lak');
IR2 = iradon(R, theta,'cubic');

I2 = cat(4,IR,IRf,IR2);

IR = permute((uint8(cat(3,IR,IR,IR)*255)),[2 1 3]);
IRf = permute((uint8(cat(3,IRf,IRf,IRf)*255)),[2 1 3]);
IR2 = permute((uint8(cat(3,IR2,IR2,IR2)*255)),[2 1 3]);

Is = squeeze(I2)*255;

[m, n, z, ~] = size(Is);

fz = sliceZDim*z;
fPlanx = pDim*m;
fPlany = pDim*n;

[x,y,z,D] = subvolume(0:pDim:fPlanx - pDim,0:pDim:fPlany - pDim,0:sliceZDim:fz-sliceZDim,Is,[0,nan,0,nan,0,nan]);
p1 = patch(isosurface(x,y,z,D, 50),'FaceColor','w','EdgeColor','k');
% 
% [x,y,z,D] = subvolume(Is,[0,m,0,n,0,z]);
% p1 = patch(isosurface(x,y,z,D, 50),'FaceColor','w','EdgeColor','k');

isonormals(x,y,z,D,p1);

p2 = patch(isocaps(x,y,z,D, 30),'FaceColor','interp','EdgeColor','none');

view(3);  

% close all

axis tight;
daspect([1 1 1]);
colormap(gray(32));
camlight right; 
camlight left; 
lighting gouraud;

saveas(p2,'3d','jpg')

% close all

IR = double(IR);
IRf = double(IRf);
IR2 = double(IR2);

t = toc;
format long g;
t = round(t*100)/100;

end