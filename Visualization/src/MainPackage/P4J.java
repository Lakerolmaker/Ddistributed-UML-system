package MainPackage;

import java.io.File;
import java.util.Arrays;

import com.google.gson.Gson;

import FileClasses.Progress;
import TCP.RunnableArg;
import TCP.TCP;
import ar.com.hjg.pngj.IImageLine;
import ar.com.hjg.pngj.ImageInfo;
import ar.com.hjg.pngj.ImageLineHelper;
import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngReader;
import ar.com.hjg.pngj.PngWriter;
import ar.com.hjg.pngj.chunks.ChunkCopyBehaviour;
import ar.com.hjg.pngj.chunks.ChunkLoadBehaviour;
import ar.com.hjg.pngj.chunks.PngChunkTextVar;

public class P4J {

	public TCP progress_tcp = new TCP();
	public Gson gson = new Gson();

	
	
	// : Sample code from the pngj library.
	public void mergeImages(String tiles[], File dest, int nTilesX) {
		int ntiles = tiles.length;
		int nTilesY = (ntiles + nTilesX - 1) / nTilesX; // integer ceil
		ImageInfo imi1, imi2; // 1:small tile 2:big image
		PngReader pngr = new PngReader(new File(tiles[0]));
		imi1 = pngr.imgInfo;
		PngReader[] readers = new PngReader[nTilesX];
		imi2 = new ImageInfo(imi1.cols * nTilesX, imi1.rows * nTilesY, imi1.bitDepth, imi1.alpha, imi1.greyscale,
				imi1.indexed);
		PngWriter pngw = new PngWriter(dest, imi2, true);
		// copy palette and transparency if necessary (more chunks?)
		pngw.copyChunksFrom(pngr.getChunksList(),
				ChunkCopyBehaviour.COPY_PALETTE | ChunkCopyBehaviour.COPY_TRANSPARENCY);
		pngr.readSkippingAllRows(); // reads only metadata
		pngr.end(); // close, we'll reopen it again soon
		ImageLineInt line2 = new ImageLineInt(imi2);
		int row2 = 0;
		for (int ty = 0; ty < nTilesY; ty++) {
			int nTilesXcur = ty < nTilesY - 1 ? nTilesX : ntiles - (nTilesY - 1) * nTilesX;
			Arrays.fill(line2.getScanline(), 0);
			for (int tx = 0; tx < nTilesXcur; tx++) { // open several readers
				readers[tx] = new PngReader(new File(tiles[tx + ty * nTilesX]));
				readers[tx].setChunkLoadBehaviour(ChunkLoadBehaviour.LOAD_CHUNK_NEVER);
				if (!readers[tx].imgInfo.equals(imi1))
					throw new RuntimeException("different tile ? " + readers[tx].imgInfo);
			}
			for (int row1 = 0; row1 < imi1.rows; row1++, row2++) {
				for (int tx = 0; tx < nTilesXcur; tx++) {
					ImageLineInt line1 = (ImageLineInt) readers[tx].readRow(row1); // read line
					System.arraycopy(line1.getScanline(), 0, line2.getScanline(), line1.getScanline().length * tx,
							line1.getScanline().length);
				}
				pngw.writeRow(line2, row2); // write to full image
			}
			for (int tx = 0; tx < nTilesXcur; tx++)
				readers[tx].end(); // close readers
			
			int procentage = getProcetage(ty , nTilesY -1);
			sendProgress(procentage , "Merging");
		}
		pngw.end(); // close writer
	}

	public void sendProgress(int progress, String stage) {

		progress_tcp.client.onConnect(new RunnableArg<String>() {

			@Override
			public void run() {
				Progress prog = new Progress(progress, stage);
				String json = gson.toJson(prog);
				progress_tcp.client.send(json);
			}

		});

		try {
			progress_tcp.client.connectTNetwork("progress_server");
		} catch (Exception e) {
		}
	}
	
	public int getProcetage(int a, int b) {
		return (int) (a * 100.0f) / b;
	}


}
