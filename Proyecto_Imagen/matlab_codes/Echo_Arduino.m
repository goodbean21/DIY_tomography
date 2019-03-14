if ~isempty(instrfind)
     fclose(instrfind);
      delete(instrfind);
end
NUM_STEPS = 48;
NUM_DETECTORS = 2;
NUM_SLICES = 3;
ard=serial('COM6','BaudRate',9600, 'Terminator', '/'); % create serial communication object 
fopen(ard); % initiate arduino communication
pause(3); % Importante esperar a la conexion!!
%{
volume = zeros([NUM_STEPS, NUM_DETECTORS, NUM_SLICES]);
pause(3);   % Importante esperar a la conexion!!
fprintf(ard, '%c', 'm'); % Comando para mandar echo de matriz
pause(2);
%fprintf(ard, '%c', 'n'); % Comando para mandar echo de matriz
while ard.BytesAvailable == 0 % Esperar respuesta
    pause(1);
end
disp('READING');
%disp(fscanf(ard, '%e'));
%sinograms = [sinograms ReadMatrix(ard, 48, 3)];
volume(:,:,1) = ReadMatrix(ard, 48, 3);
%fprintf(ard, '%c', 'n'); % Comando para mandar echo de matriz

while ard.BytesAvailable == 0 % Esperar respuesta
    pause(1);
end
disp('READING');
%disp(fscanf(ard, '%e'));
%sinograms = [sinograms ReadMatrix(ard, 48, 3)];
volume(:,:,2) = ReadMatrix(ard, 48, 3);
while ard.BytesAvailable == 0 % Esperar respuesta
    pause(1);
end
disp('READING');
%disp(fscanf(ard, '%e'));
%sinograms = [sinograms ReadMatrix(ard, 48, 3)];
volume(:,:,3) = ReadMatrix(ard, 48, 3);
%}
j = 1;
p = 1; 
vol = ReadVolume(ard, NUM_DETECTORS, NUM_STEPS, NUM_SLICES);
    
vol2 = permute(imresize(vol,[NUM_SLICES*10, 100]),[2 1 3]);

for i = 1: NUM_SLICES
    IR(:,:,i) = iradon(vol(:,:,i), 3.2,'Ram-Lak',10,100)*255;
    
end

IR = permute(IR, [2 1 3]);

%{
img = iradon(sinograma', 7.5);
subplot(1, 2, 1);

imshow(sinograma);
title('Sinograma', 'FontWeight', 'bold', 'FontSize', 16);
ylabel('Proyectores');
xlabel('Theta');
subplot(1, 2, 2);
imshow(img);
title('Reconstrucci�n', 'FontWeight', 'bold', 'FontSize', 16);
%}
fclose(ard);

