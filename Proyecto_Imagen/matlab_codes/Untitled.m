
%%
close all 

n = 100;
f = 0.9;

vol(2,1:48,1:5) = mean(baselineSinogram(:,:,1:5));
vacio(2,1:48,1:5) = mean(baselineSinogram(:,:,1:5));
unCableCentro(2,1:48,1:5) = mean(baselineSinogram(:,:,1:5));
unCableLAdo(2,1:48,1:5) = mean(baselineSinogram(:,:,1:5));
unaBolaLado(2,1:48,1:5) = mean(baselineSinogram(:,:,1:5));

vacio(4,1:48,1:5) = (baselineSinogram(1) + vacio(1))/2;
unCableCentro(4,1:48,1:5) = (baselineSinogram(1) + unCableCentro(1))/2;
unCableLado(4,1:48,1:5) = (baselineSinogram(1) + unCableLado(1))/2;
unaBolaLado(4,1:48,1:5) = (baselineSinogram(1) + unaBolaLado(1))/2;

for i = 1:5
    IRvol(:,:,i) = imopen(iradon(vol(:,:,i), 0:180/48:179,'Hann', f, n),strel('disk', 1));
    IRvacio(:,:,i) = imopen(iradon(vacio(:,:,i), 0:180/48:179,'Hann', f, n),strel('disk', 1));
    IRcabcen(:,:,i) = imopen(iradon(unCableCentro(:,:,i), 0:180/48:179,'Hann', f, n),strel('disk', 1));
    IRcablad(:,:,i) = imopen(iradon(unCableLado(:,:,i),0:180/48:179,'Hann',f,n),strel('disk', 1));
    IRbola(:,:,i) = imopen(iradon(unaBolaLado(:,:,i),0:180/48:179,'Hann',f,n),strel('disk', 1));
    IRbase(:,:,i) = imopen(iradon(baselineSinogram(:,:,i),0:180/48:179,'Hann',f,n),strel('disk', 1));
    
end


for i = 1:5
    G(i) = graythresh(uint8(baselineSinogram(:,:,i)));

end

colormap(gray(12))

figure, 
for i = 1:5
    subplot(2,3,i), imshow(IRvol(:,:,i),[])

end

figure,
for i = 1:5
    subplot(2,3,i), imshow(IRvacio(:,:,i),[])

end

figure,

for i = 1:5
    subplot(2,3,i), imshow(IRcabcen(:,:,i),[])

end

figure,
for i = 1:5
    subplot(2,3,i), imshow(IRcablad(:,:,i),[])

end

figure,
for i = 1:5
    subplot(2,3,i), imshow(IRbola(:,:,i),[])

end

figure,
for i = 1:5
    subplot(2,3,i), imshow(IRbase(:,:,i),[])

end

for i = 1:5
    corr(:,:,i) = corrcoef(IRbase,IRcabcen);
    
end


