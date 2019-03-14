function simulation3D
%%
clear all
load mri

d = D;

D = squeeze(D);

[x,y,z,D] = subvolume(D,[0,nan,0,nan,0,nan]);
p1 = patch(isosurface(x,y,z,D, 5),...
     'FaceColor','w','EdgeColor','k');

 isonormals(x,y,z,D,p1);

p2 = patch(isocaps(x,y,z,D, 5),...
     'FaceColor','interp','EdgeColor','none');
 
view(3); 
axis tight; 
daspect([1 1 0.4])
colormap(gray(100))
camlight right; 
camlight left; 
lighting gouraud

end